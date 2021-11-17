package li.naska.bgg.repository;

import com.boardgamegeek.enums.HotItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggHotRepository {

  private final WebClient webClient;

  public BggHotRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.hot}") String hotEndpoint) {
    this.webClient = builder.baseUrl(hotEndpoint).build();
  }

  public Mono<String> getItems(HotItemType type) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParam("type", type.value())
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class);
  }

}
