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
public class BggListitemsV4Repository {

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  public BggListitemsV4Repository(
      @Value("${bgg.endpoints.v4.listitems}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<BggListitemsV4ResponseBody> getListitems(BggListitemsV4QueryParams params) {
    return getListitemsAsJson(params)
        .map(body -> jsonProcessor.toJavaObject(body, BggListitemsV4ResponseBody.class));
  }

  public Mono<String> getListitemsAsJson(BggListitemsV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        });
  }

  public Mono<BggListitemV4ResponseBody> getListitem(Integer id) {
    return getListitemAsJson(id)
        .map(body -> jsonProcessor.toJavaObject(body, BggListitemV4ResponseBody.class));
  }

  public Mono<String> getListitemAsJson(Integer id) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/{id}").build(id))
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Listitem not found");
          } else if (clientResponse.statusCode() != HttpStatus.OK) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        });
  }

  public Mono<BggListitemCommentsV4ResponseBody> getListitemComments(
      Integer id, BggListitemCommentsV4QueryParams params) {
    return getListitemCommentsAsJson(id, params)
        .map(body -> jsonProcessor.toJavaObject(body, BggListitemCommentsV4ResponseBody.class));
  }

  public Mono<String> getListitemCommentsAsJson(
      Integer id, BggListitemCommentsV4QueryParams params) {
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

  public Mono<BggListitemReactionsV4ResponseBody> getListitemReactions(
      Integer id, BggListitemReactionsV4QueryParams params) {
    return getListitemReactionsAsJson(id, params)
        .map(body -> jsonProcessor.toJavaObject(body, BggListitemReactionsV4ResponseBody.class));
  }

  public Mono<String> getListitemReactionsAsJson(
      Integer id, BggListitemReactionsV4QueryParams params) {
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

  public Mono<BggListitemTipsV4ResponseBody> getListitemTips(
      Integer id, BggListitemTipsV4QueryParams params) {
    return getListitemTipsAsJson(id, params)
        .map(body -> jsonProcessor.toJavaObject(body, BggListitemTipsV4ResponseBody.class));
  }

  public Mono<String> getListitemTipsAsJson(Integer id, BggListitemTipsV4QueryParams params) {
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
