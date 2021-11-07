package li.naska.bgg.repository;

import com.boardgamegeek.collection.Collection;
import com.boardgamegeek.enums.ObjectSubtype;
import li.naska.bgg.exception.BggResponseNotReadyException;
import li.naska.bgg.repository.model.BggUserItemsParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
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

  public Mono<Collection> getItems(BggUserItemsParameters params) {
    // handle subtype bug in the BBG XML API
    if (params.getSubtype() == null || params.getSubtype().equals(ObjectSubtype.BOARDGAME)) {
      params.setExcludesubtype(ObjectSubtype.BOARDGAMEEXPANSION);
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
        .bodyToMono(Collection.class)
        .retryWhen(
            Retry.backoff(6, Duration.ofSeconds(5))
                .filter(throwable -> throwable instanceof BggResponseNotReadyException));
  }

}
