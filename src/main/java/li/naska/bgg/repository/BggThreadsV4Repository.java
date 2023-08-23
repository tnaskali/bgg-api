package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import li.naska.bgg.repository.model.BggThreadReactionsV4QueryParams;
import li.naska.bgg.repository.model.BggThreadReactionsV4ResponseBody;
import li.naska.bgg.repository.model.BggThreadV4ResponseBody;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggThreadsV4Repository {

  @Autowired
  private ObjectMapper objectMapper;

  private final WebClient webClient;

  public BggThreadsV4Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v4.threads}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<BggThreadV4ResponseBody> getThread(Integer id) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{id}")
            .build(id))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .toEntity(String.class)
        .<BggThreadV4ResponseBody>handle((entity, sink) -> {
          try {
            sink.next(objectMapper.readValue(entity.getBody(), BggThreadV4ResponseBody.class));
          } catch (JsonProcessingException e) {
            sink.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
          }
        });
  }

  public Mono<BggThreadReactionsV4ResponseBody> getThreadReactions(Integer id, BggThreadReactionsV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{id}/reactions")
            .queryParams(QueryParameters.fromPojo(params))
            .build(id))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(
            httpStatus -> httpStatus == HttpStatus.BAD_REQUEST,
            clientResponse -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown remote error")))
        .toEntity(String.class)
        .<BggThreadReactionsV4ResponseBody>handle((entity, sink) -> {
          try {
            sink.next(objectMapper.readValue(entity.getBody(), BggThreadReactionsV4ResponseBody.class));
          } catch (JsonProcessingException e) {
            sink.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
          }
        });
  }

}
