package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedServerResponseException;
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
public class BggCommentsV4Repository {

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  public BggCommentsV4Repository(
      @Value("${bgg.endpoints.v4.comments}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<BggCommentV4ResponseBody> getComment(Integer id) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/{id}").build(id))
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
          } else if (clientResponse.statusCode() != HttpStatus.OK) {
            return UnexpectedServerResponseException.from(clientResponse).buildAndThrow();
          }
          return clientResponse
              .bodyToMono(String.class)
              .defaultIfEmpty("")
              .map(body -> jsonProcessor.toJavaObject(body, BggCommentV4ResponseBody.class));
        });
  }

  public Mono<BggCommentReactionsV4ResponseBody> getCommentReactions(
      Integer id, BggCommentReactionsV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{id}/reactions")
            .queryParams(QueryParameters.fromPojo(params))
            .build(id))
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK) {
            return UnexpectedServerResponseException.from(clientResponse).buildAndThrow();
          }
          return clientResponse
              .bodyToMono(String.class)
              .defaultIfEmpty("")
              .map(body ->
                  jsonProcessor.toJavaObject(body, BggCommentReactionsV4ResponseBody.class));
        });
  }

  public Mono<BggCommentTipsV4ResponseBody> getCommentTips(
      Integer id, BggCommentTipsV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{id}/tips")
            .queryParams(QueryParameters.fromPojo(params))
            .build(id))
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK) {
            return UnexpectedServerResponseException.from(clientResponse).buildAndThrow();
          }
          return clientResponse
              .bodyToMono(String.class)
              .defaultIfEmpty("")
              .map(body -> jsonProcessor.toJavaObject(body, BggCommentTipsV4ResponseBody.class));
        });
  }
}
