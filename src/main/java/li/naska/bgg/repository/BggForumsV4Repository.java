package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import li.naska.bgg.repository.model.BggForumsThreadsV4QueryParams;
import li.naska.bgg.repository.model.BggForumsThreadsV4ResponseBody;
import li.naska.bgg.repository.model.BggForumsV4QueryParams;
import li.naska.bgg.repository.model.BggForumsV4ResponseBody;
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
public class BggForumsV4Repository {

  @Autowired
  private ObjectMapper objectMapper;

  private final WebClient webClient;

  public BggForumsV4Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v4.forums}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<BggForumsV4ResponseBody> getForums(BggForumsV4QueryParams params) {
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
        .<BggForumsV4ResponseBody>handle((entity, sink) -> {
          try {
            sink.next(objectMapper.readValue(entity.getBody(), BggForumsV4ResponseBody.class));
          } catch (JsonProcessingException e) {
            sink.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
          }
        });
  }


  public Mono<BggForumsThreadsV4ResponseBody> getThreads(BggForumsThreadsV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/threads")
            .queryParams(QueryParameters.fromPojo(params))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(
            httpStatus -> httpStatus == HttpStatus.BAD_REQUEST,
            clientResponse -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown remote error")))
        .toEntity(String.class)
        .<BggForumsThreadsV4ResponseBody>handle((entity, sink) -> {
          try {
            sink.next(objectMapper.readValue(entity.getBody(), BggForumsThreadsV4ResponseBody.class));
          } catch (JsonProcessingException e) {
            sink.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
          }
        });
  }

}
