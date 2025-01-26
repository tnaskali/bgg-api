package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.*;
import li.naska.bgg.util.JsonProcessor;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggThreadsV4Repository {

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  public BggThreadsV4Repository(
      @Value("${bgg.endpoints.v4.threads}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<BggThreadsV4ResponseBody> getThreads(BggThreadsV4QueryParams params) {
    return getThreadsAsJson(params)
        .map(body -> jsonProcessor.toJavaObject(body, BggThreadsV4ResponseBody.class));
  }

  public Mono<String> getThreadsAsJson(BggThreadsV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
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

  public Mono<BggThreadV4ResponseBody> getThread(Integer id) {
    return getThreadAsJson(id)
        .map(body -> jsonProcessor.toJavaObject(body, BggThreadV4ResponseBody.class));
  }

  public Mono<String> getThreadAsJson(Integer id) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/{id}").build(id))
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found");
          } else if (clientResponse.statusCode() != HttpStatus.OK
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

  public Mono<BggThreadReactionsV4ResponseBody> getThreadReactions(
      Integer id, BggThreadReactionsV4QueryParams params) {
    return getThreadReactionsAsJson(id, params)
        .map(body -> jsonProcessor.toJavaObject(body, BggThreadReactionsV4ResponseBody.class));
  }

  public Mono<String> getThreadReactionsAsJson(Integer id, BggThreadReactionsV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{id}/reactions")
            .queryParams(QueryParameters.fromPojo(params))
            .build(id))
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
