package li.naska.bgg.repository;

import li.naska.bgg.repository.model.BggFamiliesQueryParams;
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
 * Family Items
 * <p>
 * In the BGG database, more abstract or esoteric concepts are represented by something called a family. The XMLAPI2
 * supports families of the following FAMILYTYPEs:
 * <ul>
 *   <li>rpg
 *   <li>rpgperiodical
 *   <li>boardgamefamily
 * </ul>
 * <p>
 * Base URI: /xmlapi2/family?parameters
 *
 * @see <a href="https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc4">BGG_XML_API2</a>
 */
@Repository
public class BggFamiliesRepository {

  private final WebClient webClient;

  public BggFamiliesRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.family}") String familyEndpoint) {
    this.webClient = builder.baseUrl(familyEndpoint).build();
  }

  public Mono<String> getFamilies(BggFamiliesQueryParams params) {
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
