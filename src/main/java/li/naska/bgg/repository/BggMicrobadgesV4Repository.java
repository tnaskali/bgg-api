package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import li.naska.bgg.repository.model.BggMicrobadgesV4ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggMicrobadgesV4Repository {

  @Autowired private ObjectMapper objectMapper;

  private final WebClient webClient;

  public BggMicrobadgesV4Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v4.microbadges}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<BggMicrobadgesV4ResponseBody> getMicrobadge(Integer id) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/{id}").build(id))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .toEntity(String.class)
        .<BggMicrobadgesV4ResponseBody>handle(
            (entity, sink) -> {
              try {
                sink.next(
                    objectMapper.readValue(entity.getBody(), BggMicrobadgesV4ResponseBody.class));
              } catch (JsonProcessingException e) {
                sink.error(
                    new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
              }
            });
  }
}
