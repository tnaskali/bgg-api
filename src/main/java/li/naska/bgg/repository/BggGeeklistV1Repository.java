package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.repository.model.BggGeeklistV1QueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class BggGeeklistV1Repository {

  private final WebClient webClient;

  public BggGeeklistV1Repository(
      @Value("${bgg.endpoints.v1.geeklist}") String endpoint, WebClient.Builder builder) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<String> getGeeklist(Integer id, BggGeeklistV1QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .path("/{id}")
            .build(id))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class);
  }
}
