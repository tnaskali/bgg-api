package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedServerResponseException;
import li.naska.bgg.repository.model.BggFansV4QueryParams;
import li.naska.bgg.repository.model.BggFansV4RequestBody;
import li.naska.bgg.repository.model.BggFansV4ResponseBody;
import li.naska.bgg.util.JsonProcessor;
import li.naska.bgg.util.QueryParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class BggFansV4Repository {

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  public BggFansV4Repository(
      @Value("${bgg.endpoints.v4.fans}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<BggFansV4ResponseBody> getFans(BggFansV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK) {
            return UnexpectedServerResponseException.from(clientResponse).buildAndThrow();
          }
          return clientResponse
              .bodyToMono(String.class)
              .defaultIfEmpty("")
              .map(body -> jsonProcessor.toJavaObject(body, BggFansV4ResponseBody.class));
        });
  }

  public Mono<BggFansV4ResponseBody> createFan(String cookie, BggFansV4RequestBody requestBody) {
    return webClient
        .post()
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .header(HttpHeaders.COOKIE, cookie)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestBody)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK) {
            return UnexpectedServerResponseException.from(clientResponse).buildAndThrow();
          }
          return clientResponse
              .bodyToMono(String.class)
              .defaultIfEmpty("")
              .map(body -> jsonProcessor.toJavaObject(body, BggFansV4ResponseBody.class))
              .doOnNext(responseBody -> {
                if (responseBody.getFanid() == 0) {
                  throw new ResponseStatusException(HttpStatus.CONFLICT, "Fan already exists");
                }
              });
        });
  }

  public Mono<BggFansV4ResponseBody> deleteFan(String cookie, Integer fanid) {
    return webClient
        .delete()
        .uri(uriBuilder -> uriBuilder.path("/{id}").build(fanid))
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .header(HttpHeaders.COOKIE, cookie)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK) {
            return UnexpectedServerResponseException.from(clientResponse).buildAndThrow();
          }
          return clientResponse
              .bodyToMono(String.class)
              .defaultIfEmpty("")
              .map(body -> jsonProcessor.toJavaObject(body, BggFansV4ResponseBody.class));
        });
  }
}
