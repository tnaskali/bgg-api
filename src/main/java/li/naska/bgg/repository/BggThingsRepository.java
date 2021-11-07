package li.naska.bgg.repository;

import com.boardgamegeek.thing.Things;
import li.naska.bgg.repository.model.BggThingsParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggThingsRepository {

  private final WebClient webClient;

  public BggThingsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.thing}") String thingEndpoint) {
    this.webClient = builder.baseUrl(thingEndpoint).build();
  }

  public Mono<Things> getThings(BggThingsParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(parameters.toMultiValueMap())
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Things.class);
  }

}
