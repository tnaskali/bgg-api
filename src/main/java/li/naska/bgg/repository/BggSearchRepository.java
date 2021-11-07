package li.naska.bgg.repository;

import com.boardgamegeek.search.Results;
import li.naska.bgg.repository.model.BggSearchParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggSearchRepository {

  private final WebClient webClient;

  public BggSearchRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.search}") String searchEndpoint) {
    this.webClient = builder.baseUrl(searchEndpoint).build();
  }

  public Mono<Results> getItems(BggSearchParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(parameters.toMultiValueMap())
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Results.class);
  }

}
