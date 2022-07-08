package li.naska.bgg.repository;

import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.exception.BggResponseNotReadyException;
import li.naska.bgg.repository.model.BggCollectionV2QueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

/**
 * Collection
 * <p>
 * Request details about a user's collection.
 * <br/>
 * Note that you should check the response status code... if it's 202 (vs. 200) then it indicates BGG has queued your
 * request and you need to keep retrying (hopefully w/some delay between tries) until the status is not 202. More info
 * at <a href="https://boardgamegeek.com/thread/1188687/export-collections-has-been-updated-xmlapi-develop">Export
 * collections has been updated (XMLAPI developers read this)</a>
 * <br/>
 * Note that the default (or using subtype=boardgame) returns both boardgame and boardgameexpansion's in your
 * collection... but incorrectly gives subtype=boardgame for the expansions. Workaround is to use
 * excludesubtype=boardgameexpansion and make a 2nd call asking for subtype=boardgameexpansion
 * <p>
 * Base URI: /xmlapi2/collection?parameters
 *
 * @see <a href="https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc11">BGG_XML_API2</a>
 */
@Repository
public class BggCollectionV2Repository {

  private final WebClient webClient;

  public BggCollectionV2Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v2.collection}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<String> getCollection(String cookie, BggCollectionV2QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .headers(headers -> Optional.ofNullable(cookie)
            .ifPresent(c -> headers.add("Cookie", c)))
        .retrieve()
        .onStatus(
            // BGG might queue the request
            status -> status == HttpStatus.ACCEPTED,
            response -> Mono.error(new BggResponseNotReadyException()))
        .bodyToMono(String.class)
        .onErrorMap(IOException.class, ioe -> new BggConnectionException())
        .retryWhen(
            Retry.max(3)
                .filter(throwable -> throwable instanceof BggConnectionException))
        .retryWhen(
            Retry.backoff(4, Duration.ofSeconds(4))
                .filter(throwable -> throwable instanceof BggResponseNotReadyException))
        .doOnNext(body -> {
          if (body.equals("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>\n<errors>\n\t<error>\n\t\t<message>Invalid username specified</message>\n\t</error>\n</errors>")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Collection not found");
          }
        });
  }

}