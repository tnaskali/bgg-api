package li.naska.bgg.repository;

import li.naska.bgg.repository.model.BggGeeklistParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggGeeklistRepository {

  private final WebClient webClient;

  public BggGeeklistRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.geeklist}") String geeklistEndpoint) {
    this.webClient = builder.baseUrl(geeklistEndpoint).build();
  }

  public Mono<String> getGeeklist(BggGeeklistParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(parameters.toMultiValueMap())
            .path("/{id}")
            .build(parameters.getId()))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class);
  }

}
