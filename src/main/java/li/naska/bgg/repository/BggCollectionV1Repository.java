package li.naska.bgg.repository;

import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.exception.BggResponseNotReadyException;
import li.naska.bgg.repository.model.BggCollectionV1QueryParams;
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
 * Retrieve games in a user's collection
 * <br/>
 * <b>Important definition:</b> A user's collection includes any games the user has added to her collection on BGG. This
 * includes games she owns, games she used to own, games she's rated, games upon which she's commented, games she's
 * played, and games on her wishlist, just to name a few. In this section, references to the collection mean this
 * broader sense of BGG collection, not the user's personal stash of games.
 * <br/>
 * The XMLAPI will place requests for geeklists and user collections into a queue to be processed by backend servers.
 * When this happens, the request will result in an HTTP response of 202 to indicate the request has been queued and is
 * pending processing. These collection requests will result in an HTTP response of 200 along with the XML data once the
 * server has the data available (See
 * <a href="https://boardgamegeek.com/thread/1188687/export-collections-has-been-updated-xmlapi-develop">Export
 * collections has been updated (XMLAPI developers read this)</a> for details).
 * <p>
 * Base URI: /xmlapi/collection/{username}?parameters
 *
 * @see <a href="https://boardgamegeek.com/wiki/page/BGG_XML_API#toc5">BGG_XML_API</a>
 */
@Repository
public class BggCollectionV1Repository {

  private final WebClient webClient;

  public BggCollectionV1Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v1.collection}") String collectionEndpoint) {
    this.webClient = builder.baseUrl(collectionEndpoint).build();
  }

  public Mono<String> getCollection(String cookie, String username, BggCollectionV1QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{username}")
            .queryParams(QueryParameters.fromPojo(params))
            .build(username))
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
