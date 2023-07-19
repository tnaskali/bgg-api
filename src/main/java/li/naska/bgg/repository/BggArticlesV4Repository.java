package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.repository.model.BggArticleV4ResponseBody;
import li.naska.bgg.repository.model.BggArticlesV4QueryParams;
import li.naska.bgg.repository.model.BggArticlesV4ResponseBody;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Repository
public class BggArticlesV4Repository {

  @Autowired
  private ObjectMapper objectMapper;

  private final WebClient webClient;

  public BggArticlesV4Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v4.articles}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<BggArticlesV4ResponseBody> getArticles(BggArticlesV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(
            httpStatus -> httpStatus == HttpStatus.BAD_REQUEST,
            clientResponse -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown remote error")))
        .toEntity(String.class)
        .<BggArticlesV4ResponseBody>handle((entity, sink) -> {
          try {
            sink.next(objectMapper.readValue(entity.getBody(), BggArticlesV4ResponseBody.class));
          } catch (JsonProcessingException e) {
            sink.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
          }
        })
        .onErrorMap(IOException.class, ioe -> new BggConnectionException())
        .retryWhen(
            Retry.max(3)
                .filter(throwable -> throwable instanceof BggConnectionException));
  }

  public Mono<BggArticleV4ResponseBody> getArticle(Integer id) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{id}")
            .build(id))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .toEntity(String.class)
        .<BggArticleV4ResponseBody>handle((entity, sink) -> {
          try {
            sink.next(objectMapper.readValue(entity.getBody(), BggArticleV4ResponseBody.class));
          } catch (JsonProcessingException e) {
            sink.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
          }
        })
        .onErrorMap(IOException.class, ioe -> new BggConnectionException())
        .retryWhen(
            Retry.max(3)
                .filter(throwable -> throwable instanceof BggConnectionException));
  }

}
