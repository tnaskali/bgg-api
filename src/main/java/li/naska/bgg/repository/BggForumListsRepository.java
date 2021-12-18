package li.naska.bgg.repository;

import li.naska.bgg.repository.model.BggForumsQueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Forum Lists
 * <p>
 * You can request a list of forums for a particular type/id through the XMLAPI2.
 * <p>
 * Base URI: /xmlapi2/forumlist?parameters
 *
 * @see <a href="https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc5">BGG_XML_API2</a>
 */
@Repository
public class BggForumListsRepository {

  private final WebClient webClient;

  public BggForumListsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.forumlist}") String forumListEndpoint) {
    this.webClient = builder.baseUrl(forumListEndpoint).build();
  }

  public Mono<String> getForums(BggForumsQueryParams params) {
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
