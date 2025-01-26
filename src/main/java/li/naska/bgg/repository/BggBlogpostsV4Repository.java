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
public class BggBlogpostsV4Repository {

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  public BggBlogpostsV4Repository(
      @Value("${bgg.endpoints.v4.blogposts}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<BggBlogpostsV4ResponseBody> getBlogposts(BggBlogpostsV4QueryParams params) {
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
          return clientResponse
              .bodyToMono(String.class)
              .defaultIfEmpty("")
              .map(body -> jsonProcessor.toJavaObject(body, BggBlogpostsV4ResponseBody.class));
        });
  }

  public Mono<BggBlogpostV4ResponseBody> getBlogpost(Integer id) {
    return getBlogpostAsJson(id)
        .map(body -> jsonProcessor.toJavaObject(body, BggBlogpostV4ResponseBody.class));
  }

  public Mono<String> getBlogpostAsJson(Integer id) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/{id}").build(id))
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Blogpost not found");
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

  public Mono<BggBlogpostCommentsV4ResponseBody> getBlogpostComments(
      Integer id, BggBlogpostCommentsV4QueryParams params) {
    return getBlogpostCommentsAsJson(id, params)
        .map(body -> jsonProcessor.toJavaObject(body, BggBlogpostCommentsV4ResponseBody.class));
  }

  public Mono<String> getBlogpostCommentsAsJson(
      Integer id, BggBlogpostCommentsV4QueryParams params) {
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

  public Mono<BggBlogpostReactionsV4ResponseBody> getBlogpostReactions(
      Integer id, BggBlogpostReactionsV4QueryParams params) {
    return getBlogpostReactionsAsJson(id, params)
        .map(body -> jsonProcessor.toJavaObject(body, BggBlogpostReactionsV4ResponseBody.class));
  }

  public Mono<String> getBlogpostReactionsAsJson(
      Integer id, BggBlogpostReactionsV4QueryParams params) {
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

  public Mono<BggBlogpostTipsV4ResponseBody> getBlogpostTips(
      Integer id, BggBlogpostTipsV4QueryParams params) {
    return getBlogpostTipsAsJson(id, params)
        .map(body -> jsonProcessor.toJavaObject(body, BggBlogpostTipsV4ResponseBody.class));
  }

  public Mono<String> getBlogpostTipsAsJson(Integer id, BggBlogpostTipsV4QueryParams params) {
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
