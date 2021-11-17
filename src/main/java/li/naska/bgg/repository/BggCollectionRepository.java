package li.naska.bgg.repository;

import com.boardgamegeek.enums.ObjectSubtype;
import li.naska.bgg.exception.BggResponseNotReadyException;
import li.naska.bgg.repository.model.BggCollectionParameters;
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

@Repository
public class BggCollectionRepository {

  private final WebClient webClient;

  public BggCollectionRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.collection}") String collectionEndpoint) {
    this.webClient = builder.baseUrl(collectionEndpoint).build();
  }

  public Mono<String> getCollection(BggCollectionParameters params) {
    // handle subtype bug in the BBG XML API
    if (params.getSubtype() == null || params.getSubtype().equals(ObjectSubtype.boardgame)) {
      params.setExcludesubtype(ObjectSubtype.boardgameexpansion);
    }

    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(params.toMultiValueMap())
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(
            // BGG might queue the request
            status -> status == HttpStatus.ACCEPTED,
            response -> Mono.error(new BggResponseNotReadyException()))
        .bodyToMono(String.class)
        .retryWhen(
            Retry.backoff(6, Duration.ofSeconds(5))
                .filter(throwable -> throwable instanceof BggResponseNotReadyException))
        .doOnNext(body -> {
          if (body.equals("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>\n<errors>\n\t<error>\n\t\t<message>Invalid username specified</message>\n\t</error>\n</errors>")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Collection not found");
          }
        });
  }

}
