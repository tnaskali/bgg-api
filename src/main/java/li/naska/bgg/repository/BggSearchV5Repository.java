package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import li.naska.bgg.repository.model.BggSearchV5QueryParams;
import li.naska.bgg.repository.model.BggSearchV5ResponseBody;
import li.naska.bgg.resource.v5.model.SearchContext;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggSearchV5Repository {

  private final WebClient webClient;
  private final ObjectMapper objectMapper;

  public BggSearchV5Repository(
      @Value("${bgg.endpoints.v5.search}") String endpoint,
      WebClient.Builder builder,
      ObjectMapper objectMapper) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.objectMapper = objectMapper;
  }

  public Mono<BggSearchV5ResponseBody> getSearchResults(
      SearchContext context, BggSearchV5QueryParams params) {
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
        .handle((entity, sink) -> {
          try {
            sink.next(objectMapper.readValue(entity.getBody(), BggSearchV5ResponseBody.class));
          } catch (JsonProcessingException e) {
            sink.error(
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
          }
        });
  }
}
