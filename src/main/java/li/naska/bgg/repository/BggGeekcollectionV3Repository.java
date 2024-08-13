package li.naska.bgg.repository;

import li.naska.bgg.exception.BggResponseNotReadyException;
import li.naska.bgg.repository.model.BggGeekcollectionV3QueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class BggGeekcollectionV3Repository {

  private final WebClient webClient;

  public BggGeekcollectionV3Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v3.geekcollection}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<String> getGeekcollection(Optional<String> cookie, BggGeekcollectionV3QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .build())
        .accept(MediaType.valueOf("text/comma-separated-values"))
        .acceptCharset(StandardCharsets.UTF_8)
        .headers(headers -> cookie.ifPresent(c -> headers.add(HttpHeaders.COOKIE, c)))
        .retrieve()
        .toEntity(String.class)
        .doOnNext(entity -> {
              if (MediaType.TEXT_HTML.equalsTypeAndSubtype(entity.getHeaders().getContentType())) {
                Matcher matcher = Pattern.compile("<div class='messagebox error'>([\\s\\S]*?)</div>").matcher(entity.getBody());
                if (matcher.find()) {
                  String error = matcher.group(1).trim();
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
