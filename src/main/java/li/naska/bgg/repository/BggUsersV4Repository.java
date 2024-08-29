package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.List;
import li.naska.bgg.repository.model.BggUsersV4QueryParams;
import li.naska.bgg.repository.model.BggUsersV4ResponseBody;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggUsersV4Repository {

  @Autowired
  private ObjectMapper objectMapper;

  private final WebClient webClient;

  public BggUsersV4Repository(
      @Autowired WebClient.Builder builder, @Value("${bgg.endpoints.v4.users}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<List<BggUsersV4ResponseBody>> getUsers(BggUsersV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(
            httpStatus -> httpStatus == HttpStatus.BAD_REQUEST,
            clientResponse -> Mono.error(
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown remote error")))
        .toEntity(String.class)
        .<List<BggUsersV4ResponseBody>>handle((entity, sink) -> {
          try {
            sink.next(objectMapper.readValue(entity.getBody(), new TypeReference<>() {}));
          } catch (JsonProcessingException e) {
            sink.error(
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
          }
        });
  }

  public Mono<BggUsersV4ResponseBody> getUser(Integer id) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/{id}").build(id))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .toEntity(String.class)
        .<BggUsersV4ResponseBody>handle((entity, sink) -> {
          try {
            sink.next(objectMapper.readValue(entity.getBody(), BggUsersV4ResponseBody.class));
          } catch (JsonProcessingException e) {
            sink.error(
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
          }
        });
  }
}
