package li.naska.bgg.repository;

import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.exception.BggResponseNotReadyException;
import li.naska.bgg.repository.model.BggGeekcollectionQueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class BggGeekcollectionsRepository {

  private final WebClient geekcollectionWebClient;

  public BggGeekcollectionsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.geekcollection}") String geekcollectionEndpoint) {
    this.geekcollectionWebClient = builder.baseUrl(geekcollectionEndpoint).build();
  }

  public Mono<String> getGeekcollection(String cookie, BggGeekcollectionQueryParams params) {
    return geekcollectionWebClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .build())
        .header("Accept", "text/comma-separated-values")
        .headers(headers -> Optional.ofNullable(cookie)
            .ifPresent(c -> headers.add("Cookie", c)))
        .acceptCharset(StandardCharsets.UTF_8)
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
                  if ("Your request for this collection has been accepted and will be processed.".equals(error)) {
                    throw new BggResponseNotReadyException();
                  } else if ("Invalid username specified".equals(error)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username");
                  } else if ("Invalid action".equals(error)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action");
                  } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error);
                  }
                } else {
                  throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "BGG Service error");
                }
              }
            }
        )
        .retryWhen(
            Retry.backoff(4, Duration.ofSeconds(4))
                .filter(throwable -> throwable instanceof BggResponseNotReadyException))
        .map(HttpEntity::getBody);
  }

}
