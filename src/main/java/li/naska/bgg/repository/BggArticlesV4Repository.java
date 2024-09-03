package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedServerResponseException;
import li.naska.bgg.repository.model.BggArticleV4ResponseBody;
import li.naska.bgg.repository.model.BggArticlesV4QueryParams;
import li.naska.bgg.repository.model.BggArticlesV4ResponseBody;
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
public class BggArticlesV4Repository {

  private final JsonProcessor jsonProcessor;

  private final WebClient webClient;

  public BggArticlesV4Repository(
      @Value("${bgg.endpoints.v4.articles}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<BggArticlesV4ResponseBody> getArticles(BggArticlesV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found");
          } else if (clientResponse.statusCode() != HttpStatus.OK) {
            return UnexpectedServerResponseException.from(clientResponse).buildAndThrow();
          }
          return clientResponse
              .bodyToMono(String.class)
              .defaultIfEmpty("")
              .map(body -> jsonProcessor.toJavaObject(body, BggArticlesV4ResponseBody.class))
              .doOnNext(responseBody -> {
                if (responseBody.getErrors() != null
                    && "Invalid threadid".equals(responseBody.getErrors().getError())) {
                  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid threadid");
                } else if (responseBody.getArticles().isEmpty() && responseBody.getPageid() > 1) {
                  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid pageid");
                } else if (responseBody.getErrors() != null) {
                  throw UnexpectedServerResponseException.from(
                          clientResponse.statusCode(), responseBody.toString())
                      .build();
                } else if (responseBody.getArticles().isEmpty()) {
                  throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found");
                }
              });
        });
  }

  public Mono<BggArticleV4ResponseBody> getArticle(Integer id) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/{id}").build(id))
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found");
          } else if (clientResponse.statusCode() != HttpStatus.OK) {
            return UnexpectedServerResponseException.from(clientResponse).buildAndThrow();
          }
          return clientResponse
              .bodyToMono(String.class)
              .defaultIfEmpty("")
              .map(body -> jsonProcessor.toJavaObject(body, BggArticleV4ResponseBody.class))
              .doOnNext(responseBody -> {
                if (responseBody.getErrors() != null
                    && "Invalid article".equals(responseBody.getErrors().getError())) {
                  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
                } else if (responseBody.getErrors() != null) {
                  throw UnexpectedServerResponseException.from(
                          clientResponse.statusCode(), responseBody.toString())
                      .build();
                }
              });
        });
  }
}
