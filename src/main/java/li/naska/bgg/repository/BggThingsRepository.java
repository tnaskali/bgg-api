package li.naska.bgg.repository;

import li.naska.bgg.repository.model.BggThingsQueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

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
public class BggThingsRepository {

  private final WebClient webClient;

  public BggThingsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.thing}") String thingEndpoint) {
    this.webClient = builder.baseUrl(thingEndpoint).build();
  }

  public Mono<String> getThings(BggThingsQueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(
            httpStatus -> httpStatus == HttpStatus.BAD_REQUEST,
            clientResponse -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown remote error")))
        .bodyToMono(String.class);
  }

}
