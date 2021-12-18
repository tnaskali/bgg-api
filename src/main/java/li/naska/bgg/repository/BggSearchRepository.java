package li.naska.bgg.repository;

import li.naska.bgg.repository.model.BggSearchQueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Search
 * <p>
 * You can search for items from the database by name.
 * <p>
 * Base URI: /xmlapi2/search?parameters
 *
 * @see <a href="https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc14">BGG_XML_API2</a>
 */
@Repository
public class BggSearchRepository {

  private final WebClient webClient;

  public BggSearchRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.search}") String searchEndpoint) {
    this.webClient = builder.baseUrl(searchEndpoint).build();
  }

  public Mono<String> getResults(BggSearchQueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class);
  }

}
