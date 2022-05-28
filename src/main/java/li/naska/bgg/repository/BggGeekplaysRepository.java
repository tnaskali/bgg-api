package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.repository.model.BggGeekplayRequestBody;
import li.naska.bgg.repository.model.BggGeekplayResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class BggGeekplaysRepository {

  private final WebClient geekplayWebClient;

  public BggGeekplaysRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.geekplay}") String geekplayEndpoint) {
    this.geekplayWebClient = builder.baseUrl(geekplayEndpoint).build();
  }

  public Mono<BggGeekplayResponseBody> updateGeekplay(String cookie, BggGeekplayRequestBody requestBody) {
    return geekplayWebClient
        .post()
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Cookie", cookie)
        .bodyValue(requestBody)
        .retrieve()
        .toEntity(String.class)
        .onErrorMap(IOException.class, ioe -> new BggConnectionException())
        .retryWhen(
            Retry.max(3)
                .filter(throwable -> throwable instanceof BggConnectionException))
        .doOnNext(entity -> {
              if (MediaType.TEXT_HTML.equalsTypeAndSubtype(entity.getHeaders().getContentType())) {
                Matcher matcher = Pattern.compile("<div class='messagebox error'>\\s*(.+)\\s*</div>").matcher(entity.getBody());
                if (matcher.find()) {
                  String error = matcher.group(1);
                  if ("Play does not exist.".equals(error)) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Play does not exist");
                  } else if ("You can't delete this play".equals(error)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You can't delete this play");
                  } else if ("Invalid action".equals(error)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action");
                  } else if ("You are not permitted to edit this play.".equals(error)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Operation not allowed");
                  } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error);
                  }
                } else {
                  throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "BGG Service error");
                }
              }
            }
        )
        .map(entity -> {
          try {
            return new ObjectMapper().readValue(entity.getBody(), BggGeekplayResponseBody.class);
          } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
          }
        })
        .doOnNext(responseBody -> {
          if ("delete".equals(requestBody.getAction()) && !Boolean.TRUE.equals(responseBody.getSuccess())) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                responseBody.getError() != null ? responseBody.getError() : "Error deleting play");
          } else if (responseBody.getError() != null) {
            if ("You must login to save plays".equals(responseBody.getError())) {
              throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authentication required");
            } else if ("Invalid item. Play not saved.".equals(responseBody.getError())) {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid item");
            } else if ("Invalid action".equals(responseBody.getError())) {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action");
            } else {
              throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, responseBody.getError());
            }
          }
        });
  }

}
