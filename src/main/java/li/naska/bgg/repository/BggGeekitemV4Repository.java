package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggGeekitemLinkeditemsV4QueryParams;
import li.naska.bgg.repository.model.BggGeekitemLinkeditemsV4ResponseBody;
import li.naska.bgg.repository.model.BggGeekitemRecsV4QueryParams;
import li.naska.bgg.repository.model.BggGeekitemRecsV4ResponseBody;
import li.naska.bgg.util.JsonProcessor;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class BggGeekitemV4Repository {

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  public BggGeekitemV4Repository(
      @Value("${bgg.endpoints.v4.geekitem}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<BggGeekitemLinkeditemsV4ResponseBody> getLinkeditems(
      BggGeekitemLinkeditemsV4QueryParams params) {
    return getLinkeditemsAsJson(params)
        .map(body -> jsonProcessor.toJavaObject(body, BggGeekitemLinkeditemsV4ResponseBody.class));
  }

  public Mono<String> getLinkeditemsAsJson(BggGeekitemLinkeditemsV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/linkeditems")
            .queryParams(QueryParameters.fromPojo(params))
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK
              || clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.APPLICATION_JSON::equalsTypeAndSubtype)
                  .isEmpty()) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        });
  }

  public Mono<BggGeekitemRecsV4ResponseBody> getRecs(BggGeekitemRecsV4QueryParams params) {
    return getRecsAsJson(params)
        .map(body -> jsonProcessor.toJavaObject(body, BggGeekitemRecsV4ResponseBody.class));
  }

  public Mono<String> getRecsAsJson(BggGeekitemRecsV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/recs")
            .queryParams(QueryParameters.fromPojo(params))
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK
              || clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.APPLICATION_JSON::equalsTypeAndSubtype)
                  .isEmpty()) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        });
  }
}
