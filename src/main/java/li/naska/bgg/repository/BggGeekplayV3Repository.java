package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import li.naska.bgg.repository.model.BggGeekplayV3QueryParams;
import li.naska.bgg.repository.model.BggGeekplayV3RequestBody;
import li.naska.bgg.repository.model.BggGeekplayV3ResponseBody;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class BggGeekplayV3Repository {

  @Autowired
  private ObjectMapper objectMapper;

  private final WebClient webClient;

  public BggGeekplayV3Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v3.geekplay}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<BggGeekplayV3ResponseBody> getGeekplay(String cookie, BggGeekplayV3QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .header("Cookie", cookie)
        .retrieve()
        .toEntity(String.class)
        .handle((entity, sink) -> {
          try {
            sink.next(objectMapper.readValue(entity.getBody(), BggGeekplayV3ResponseBody.class));
          } catch (JsonProcessingException e) {
            sink.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
          }
        });
  }

  public Mono<BggGeekplayV3ResponseBody> updateGeekplay(String cookie, BggGeekplayV3RequestBody requestBody) {
    return webClient
        .post()
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .contentType(MediaType.APPLICATION_JSON)
        .header("Cookie", cookie)
        .bodyValue(requestBody)
        .retrieve()
        .toEntity(String.class)
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
        .<BggGeekplayV3ResponseBody>handle((entity, sink) -> {
          try {
            sink.next(objectMapper.readValue(entity.getBody(), BggGeekplayV3ResponseBody.class));
          } catch (JsonProcessingException e) {
            sink.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
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
            } else {
              throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, responseBody.getError());
            }
          }
        });
  }

}
