package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.repository.model.BggGeekaccountV3RequestBody;
import li.naska.bgg.repository.model.BggGeekaccountV3ResponseBody;
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
public class BggGeekaccountV3Repository {

  private final WebClient webClient;

  public BggGeekaccountV3Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v3.geekaccount}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<BggGeekaccountV3ResponseBody> updateGeekaccount(String cookie, BggGeekaccountV3RequestBody requestBody) {
    return webClient
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
                Matcher matcher = Pattern.compile("<div class='messagebox'>\\s*(.+)\\s*</div>").matcher(entity.getBody());
                if (matcher.find()) {
                  String error = matcher.group(1);
                  if ("This action requires you to <a href=\"/login?redirect=1\">login</a>.".equals(error)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
                  }
                }
                matcher = Pattern.compile("<div class='messagebox error'>\\s*(.+)\\s*</div>").matcher(entity.getBody());
                if (matcher.find()) {
                  String error = matcher.group(1);
                  if ("Invalid action".equals(error)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action");
                  }
                }
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "BGG Service error");
              }
            }
        )
        .map(entity -> {
          try {
            return new ObjectMapper().readValue(entity.getBody(), BggGeekaccountV3ResponseBody.class);
          } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
          }
        })
        .doOnNext(responseBody -> {
          if (responseBody.getError() != null) {
            if ("Invalid item".equals(responseBody.getError())) {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid item");
            } else {
              throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, responseBody.getError());
            }
          }
        });
  }

}