package li.naska.bgg.repository;

import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.repository.model.BggThingV2QueryParams;
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
 * Thing Items
 * <p>
 * In the BGG database, any physical, tangible product is called a thing. The XMLAPI2 supports things of the following
 * THINGTYPEs:
 * <ul>
 *   <li>boardgame
 *   <li>boardgameexpansion
 *   <li>boardgameaccessory
 *   <li>videogame
 *   <li>rpgitem
 *   <li>rpgissue (for periodicals)
 * </ul>
 * <p>
 * Base URI: /xmlapi2/thing?parameters
 *
 * @see <a href="https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc3">BGG_XML_API2</a>
 */
@Repository
public class BggThingV2Repository {

  private final WebClient webClient;

  public BggThingV2Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v2.thing}") String thingEndpoint) {
    this.webClient = builder.baseUrl(thingEndpoint).build();
  }

  public Mono<String> getThings(BggThingV2QueryParams params) {
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
