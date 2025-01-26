package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggGeekitempollV3QueryParams;
import li.naska.bgg.repository.model.BggGeekitempollV3ResponseBody;
import li.naska.bgg.util.JsonProcessor;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class BggGeekitempollV3Repository {

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  public BggGeekitempollV3Repository(
      @Value("${bgg.endpoints.v3.geekpollitem}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<BggGeekitempollV3ResponseBody> getGeekitempoll(
      Optional<String> cookie, BggGeekitempollV3QueryParams params) {
    return getGeekitempollAsJson(cookie, params)
        .map(body -> jsonProcessor.toJavaObject(body, BggGeekitempollV3ResponseBody.class));
  }

  public Mono<String> getGeekitempollAsJson(
      Optional<String> cookie, BggGeekitempollV3QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .headers(headers -> cookie.ifPresent(c -> headers.add(HttpHeaders.COOKIE, c)))
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK
              || clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.TEXT_HTML::equalsTypeAndSubtype)
                  .isEmpty()) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        });
  }
}
