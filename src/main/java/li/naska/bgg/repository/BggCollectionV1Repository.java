package li.naska.bgg.repository;

import com.boardgamegeek.collection.v1.Items;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import li.naska.bgg.exception.BggResponseNotReadyException;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggCollectionV1QueryParams;
import li.naska.bgg.util.QueryParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Repository
public class BggCollectionV1Repository {

  private final WebClient webClient;

  private final XmlProcessor xmlProcessor;

  public BggCollectionV1Repository(
      @Value("${bgg.endpoints.v1.collection}") String endpoint,
      WebClient.Builder builder,
      XmlProcessor xmlProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.xmlProcessor = xmlProcessor;
  }

  public Mono<String> getItemsAsJson(
      Optional<String> cookie, String username, BggCollectionV1QueryParams params) {
    return getItems(cookie, username, params).map(xmlProcessor::toJsonString);
  }

  public Mono<Items> getItems(
      Optional<String> cookie, String username, BggCollectionV1QueryParams params) {
    return getItemsAsXml(cookie, username, params)
        .map(xml -> xmlProcessor.toJavaObject(xml, Items.class));
  }

  public Mono<String> getItemsAsXml(
      Optional<String> cookie, String username, BggCollectionV1QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{username}")
            .queryParams(QueryParameters.fromPojo(params))
            .build(username))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .headers(headers -> cookie.ifPresent(c -> headers.add(HttpHeaders.COOKIE, c)))
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.ACCEPTED) {
            // retry later as BGG might queue the request
            throw new BggResponseNotReadyException();
          } else if (clientResponse.statusCode() != HttpStatus.OK
              || clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.TEXT_XML::equalsTypeAndSubtype)
                  .isEmpty()) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        })
        .doOnNext(
            body -> xmlProcessor.xPathValue(body, "/errors/error/message").ifPresent(message -> {
              if (message.equals("Invalid username specified")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
              } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message);
              }
            }))
        .retryWhen(Retry.backoff(4, Duration.ofSeconds(4))
            .filter(BggResponseNotReadyException.class::isInstance));
  }
}
