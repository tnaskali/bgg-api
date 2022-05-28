package li.naska.bgg.repository;

import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.repository.model.BggHotItemsQueryParams;
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
 * Hot Items
 * <p>
 * You can retrieve the list of most active items on the site.
 * <p>
 * Base URI: /xmlapi2/hot?parameter
 *
 * @see <a href="https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc12">BGG_XML_API2</a>
 */
@Repository
public class BggHotItemsRepository {

  private final WebClient webClient;

  public BggHotItemsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.hot}") String hotEndpoint) {
    this.webClient = builder.baseUrl(hotEndpoint).build();
  }

  public Mono<String> getHotItems(BggHotItemsQueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .build())
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
