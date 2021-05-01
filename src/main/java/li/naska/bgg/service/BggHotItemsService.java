package li.naska.bgg.service;

import com.boardgamegeek.enums.HotItemType;
import com.boardgamegeek.hot.HotItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Service
public class BggHotItemsService {

  @Value("${bgg.endpoints.hot.read}")
  private String hotReadEndpoint;

  @Autowired
  public WebClient webClient;

  public ResponseEntity<HotItems> getItems(HotItemType type) {
    String url = hotReadEndpoint + String.format("?type=%s", type.value());
    return webClient.get()
            .uri(url)
            .accept(MediaType.APPLICATION_XML)
            .acceptCharset(StandardCharsets.UTF_8)
            .exchangeToMono(c -> c.toEntity(HotItems.class))
            .block();
  }

}
