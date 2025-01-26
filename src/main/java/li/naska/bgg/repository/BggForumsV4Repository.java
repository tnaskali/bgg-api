package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.*;
import li.naska.bgg.util.JsonProcessor;
import li.naska.bgg.util.QueryParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class BggForumsV4Repository {

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  public BggForumsV4Repository(
      @Value("${bgg.endpoints.v4.forums}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<BggForumV4ResponseBody> getForum(Long id) {
    return getForumAsJson(id)
        .map(body -> jsonProcessor.toJavaObject(body, BggForumV4ResponseBody.class));
  }

  public Mono<String> getForumAsJson(Long id) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.path("/{id}").queryParam("partial", "essential").build(id))
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Forum not found");
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

  public Mono<BggForumsV4ResponseBody> getForums(BggForumsV4QueryParams params) {
    return getForumsAsJson(params)
        .map(body -> jsonProcessor.toJavaObject(body, BggForumsV4ResponseBody.class));
  }

  public Mono<String> getForumsAsJson(BggForumsV4QueryParams params) {
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

  public Mono<BggForumsThreadsV4ResponseBody> getThreads(BggForumsThreadsV4QueryParams params) {
    return getThreadsAsJson(params)
        .map(body -> jsonProcessor.toJavaObject(body, BggForumsThreadsV4ResponseBody.class));
  }

  public Mono<String> getThreadsAsJson(BggForumsThreadsV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/threads")
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
