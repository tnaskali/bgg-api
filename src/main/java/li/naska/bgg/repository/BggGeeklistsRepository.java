package li.naska.bgg.repository;

import com.boardgamegeek.geeklist.Geeklist;
import li.naska.bgg.repository.model.BggGeeklistsParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggGeeklistsRepository {

  private final WebClient webClient;

  public BggGeeklistsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.geeklist}") String geeklistEndpoint) {
    this.webClient = builder.baseUrl(geeklistEndpoint).build();
  }

  public Mono<Geeklist> getGeeklist(BggGeeklistsParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(parameters.toMultiValueMap())
            .path("/{id}")
            .build(parameters.getId()))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Geeklist.class);
  }

}
