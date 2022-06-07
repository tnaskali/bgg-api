package li.naska.bgg.repository;

import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.repository.model.BggForumlistV2QueryParams;
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
 * Forum Lists
 * <p>
 * You can request a list of forums for a particular type/id through the XMLAPI2.
 * <p>
 * Base URI: /xmlapi2/forumlist?parameters
 *
 * @see <a href="https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc5">BGG_XML_API2</a>
 */
@Repository
public class BggForumlistV2Repository {

  private final WebClient webClient;

  public BggForumlistV2Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v2.forumlist}") String forumListEndpoint) {
    this.webClient = builder.baseUrl(forumListEndpoint).build();
  }

  public Mono<String> getForums(BggForumlistV2QueryParams params) {
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
