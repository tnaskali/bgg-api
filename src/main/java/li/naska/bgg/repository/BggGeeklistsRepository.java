package li.naska.bgg.repository;

import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.repository.model.BggGeeklistQueryParams;
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

/**
 * Retrieve entries from a geeklist
 * <p>
 * /xmlapi/geeklist/<listid>
 * <p>
 * Example: https://www.boardgamegeek.com/xmlapi/geeklist/11205
 *
 * @see <a href="https://boardgamegeek.com/wiki/page/BGG_XML_API&redirectedfrom=XML_API#toc7">BGG XML API</a>
 */
@Repository
public class BggGeeklistsRepository {

  private final WebClient webClient;

  public BggGeeklistsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.geeklist}") String geeklistEndpoint) {
    this.webClient = builder.baseUrl(geeklistEndpoint).build();
  }

  public Mono<String> getGeeklist(Integer id, BggGeeklistQueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .path("/{id}")
            .build(id))
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
