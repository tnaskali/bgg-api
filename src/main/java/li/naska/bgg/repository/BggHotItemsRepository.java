package li.naska.bgg.repository;

import com.boardgamegeek.enums.HotItemType;
import com.boardgamegeek.hot.HotItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggHotItemsRepository {

  private final WebClient webClient;

  public BggHotItemsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.hot}") String hotEndpoint) {
    this.webClient = builder.baseUrl(hotEndpoint).build();
  }

  public Mono<HotItems> getItems(HotItemType type) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParam("type", type.value())
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(HotItems.class);
  }

}
