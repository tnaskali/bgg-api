package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggGeekpollV3QueryParams;
import li.naska.bgg.repository.model.BggGeekpollV3ResponseBody;
import li.naska.bgg.util.JsonProcessor;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggGeekpollV3Repository {

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  public BggGeekpollV3Repository(
      @Value("${bgg.endpoints.v3.geekpoll}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<BggGeekpollV3ResponseBody> getGeekpoll(
      Optional<String> cookie, BggGeekpollV3QueryParams params) {
    return getGeekpollJson(cookie, params)
        .doOnNext(body -> jsonProcessor
            .jsonPathValue(body, "$.poll")
            .filter(value -> value.equals(Boolean.FALSE))
            .ifPresent(value -> {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Poll does not exist");
            }))
        .map(body -> jsonProcessor.toJavaObject(body, BggGeekpollV3ResponseBody.class));
  }

  public Mono<String> getGeekpollJson(Optional<String> cookie, BggGeekpollV3QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .headers(headers -> cookie.ifPresent(c -> headers.add(HttpHeaders.COOKIE, c)))
        .acceptCharset(StandardCharsets.UTF_8)
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
