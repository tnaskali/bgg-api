package li.naska.bgg.repository;

import com.boardgamegeek.collection.v2.Items;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import li.naska.bgg.exception.BggResponseNotReadyException;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggCollectionV2QueryParams;
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
public class BggCollectionV2Repository {

  private final WebClient webClient;

  private final XmlProcessor xmlProcessor;

  public BggCollectionV2Repository(
      @Value("${bgg.endpoints.v2.collection}") String endpoint,
      @Value("${bgg.application.token:UNDEFINED}") String applicationToken,
      WebClient.Builder builder,
      XmlProcessor xmlProcessor) {
    this.webClient = builder
        .baseUrl(endpoint)
        .defaultHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", applicationToken))
        .build();
    this.xmlProcessor = xmlProcessor;
  }

  public Mono<String> getItemsAsJson(Optional<String> cookie, BggCollectionV2QueryParams params) {
    return getItems(cookie, params).map(xmlProcessor::toJsonString);
  }

  public Mono<Items> getItems(Optional<String> cookie, BggCollectionV2QueryParams params) {
    return getItemsAsXml(cookie, params).map(xml -> xmlProcessor.toJavaObject(xml, Items.class));
  }

  public Mono<String> getItemsAsXml(Optional<String> cookie, BggCollectionV2QueryParams params) {
    // BBG incorrectly returns boardgameexpansion items with the wrong subtype if not explicitly
    // excluded
    if (params.getSubtype() == null || params.getSubtype().equals("boardgame")) {
      params.setExcludesubtype("boardgameexpansion");
    }
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
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
