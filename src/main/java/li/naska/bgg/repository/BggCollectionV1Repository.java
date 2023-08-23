package li.naska.bgg.repository;

import li.naska.bgg.exception.BggResponseNotReadyException;
import li.naska.bgg.repository.model.BggCollectionV1QueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Repository
public class BggCollectionV1Repository {

  private final WebClient webClient;

  public BggCollectionV1Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v1.collection}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<String> getCollection(Optional<String> cookie, String username, BggCollectionV1QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{username}")
            .queryParams(QueryParameters.fromPojo(params))
            .build(username))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .headers(headers -> cookie.ifPresent(c -> headers.add("Cookie", c)))
        .retrieve()
        .onStatus(
            // BGG might queue the request
            status -> status == HttpStatus.ACCEPTED,
            response -> Mono.error(new BggResponseNotReadyException()))
        .bodyToMono(String.class)
        .retryWhen(
            Retry.backoff(4, Duration.ofSeconds(4))
                .filter(throwable -> throwable instanceof BggResponseNotReadyException))
        .doOnNext(body -> {
          if (body.equals("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>\n<errors>\n\t<error>\n\t\t<message>Invalid username specified</message>\n\t</error>\n</errors>")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Collection not found");
          }
        });
  }

}
