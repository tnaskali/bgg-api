package li.naska.bgg.repository;

import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.repository.model.BggBoardgameV1QueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Boardgame
 * <p>
 * Retrieve information about a particular game or games
 * <p>
 * Base URI: /xmlapi/boardgame/{gameids}?parameters
 *
 * @see <a href="https://boardgamegeek.com/wiki/page/BGG_XML_API#toc4">BGG_XML_API</a>
 */
@Repository
public class BggBoardgameV1Repository {

  private final WebClient webClient;

  public BggBoardgameV1Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v1.boardgame}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<String> getResults(List<Integer> ids, BggBoardgameV1QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{gameids}")
            .queryParams(QueryParameters.fromPojo(params))
            .build(ids.stream().map(Objects::toString).collect(Collectors.joining(","))))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class)
        .onErrorMap(IOException.class, ioe -> new BggConnectionException())
        .retryWhen(
            Retry.max(3)
                .filter(throwable -> throwable instanceof BggConnectionException));
  }

}
