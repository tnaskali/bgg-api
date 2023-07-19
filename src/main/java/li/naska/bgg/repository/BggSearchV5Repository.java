package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.repository.model.BggSearchV5QueryParams;
import li.naska.bgg.repository.model.BggSearchV5ResponseBody;
import li.naska.bgg.resource.v5.model.SearchContext;
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
public class BggSearchV5Repository {

  @Autowired
  private ObjectMapper objectMapper;

  private final WebClient webClient;

  public BggSearchV5Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v5.search}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<BggSearchV5ResponseBody> getSearchResults(SearchContext context, BggSearchV5QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{context}")
            .queryParams(QueryParameters.fromPojo(params))
            .build(context))
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .toEntity(String.class)
        .onErrorMap(IOException.class, ioe -> new BggConnectionException())
        .retryWhen(
            Retry.max(3)
                .filter(throwable -> throwable instanceof BggConnectionException))
        .handle((entity, sink) -> {
          try {
            sink.next(objectMapper.readValue(entity.getBody(), BggSearchV5ResponseBody.class));
          } catch (JsonProcessingException e) {
            sink.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
          }
        });
  }

}
