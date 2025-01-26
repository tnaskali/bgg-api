package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.*;
import li.naska.bgg.util.JsonProcessor;
import li.naska.bgg.util.QueryParameters;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggGeeklistsV4Repository {

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  public BggGeeklistsV4Repository(
      @Value("${bgg.endpoints.v4.geeklists}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<BggGeeklistsV4ResponseBody> getGeeklists(BggGeeklistsV4QueryParams params) {
    return getGeeklistsAsJson(params)
        .map(body -> jsonProcessor.toJavaObject(body, BggGeeklistsV4ResponseBody.class));
  }

  public Mono<String> getGeeklistsAsJson(BggGeeklistsV4QueryParams params) {
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

  public Mono<BggGeeklistV4ResponseBody> getGeeklist(Integer id) {
    return getGeeklistAsJson(id)
        .map(body -> jsonProcessor.toJavaObject(body, BggGeeklistV4ResponseBody.class));
  }

  public Mono<String> getGeeklistAsJson(Integer id) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/{id}").build(id))
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Geeklist not found");
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

  public Mono<BggGeeklistCommentsV4ResponseBody> getGeeklistComments(
      Integer id, BggGeeklistCommentsV4QueryParams params) {
    return getGeeklistCommentsAsJson(id, params)
        .map(body -> jsonProcessor.toJavaObject(body, BggGeeklistCommentsV4ResponseBody.class));
  }

  public Mono<String> getGeeklistCommentsAsJson(
      Integer id, BggGeeklistCommentsV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{id}/comments")
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
        })
        .mapNotNull(body ->
            StringUtils.isNumeric(body) ? String.format("{ \"totalCount\": %s }", body) : body);
  }

  public Mono<BggGeeklistReactionsV4ResponseBody> getGeeklistReactions(
      Integer id, BggGeeklistReactionsV4QueryParams params) {
    return getGeeklistReactionsAsJson(id, params)
        .map(body -> jsonProcessor.toJavaObject(body, BggGeeklistReactionsV4ResponseBody.class));
  }

  public Mono<String> getGeeklistReactionsAsJson(
      Integer id, BggGeeklistReactionsV4QueryParams params) {
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

  public Mono<BggGeeklistTipsV4ResponseBody> getGeeklistTips(
      Integer id, BggGeeklistTipsV4QueryParams params) {
    return getGeeklistTipsAsJson(id, params)
        .map(body -> jsonProcessor.toJavaObject(body, BggGeeklistTipsV4ResponseBody.class));
  }

  public Mono<String> getGeeklistTipsAsJson(Integer id, BggGeeklistTipsV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{id}/tips")
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
