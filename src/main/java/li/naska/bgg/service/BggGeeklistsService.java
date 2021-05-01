package li.naska.bgg.service;

import com.boardgamegeek.geeklist.Geeklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class BggGeeklistsService {

  @Value("${bgg.endpoints.geeklist.read}")
  private String geeklistReadEndpoint;

  @Autowired
  public WebClient webClient;

  public ResponseEntity<Geeklist> getGeeklist(Integer id, Map<String, String> extraParams) {
    String urlParams = extraParams
            .entrySet()
            .stream()
            .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
            .reduce((x, y) -> String.format("%s&%s", x, y))
            .map(s -> String.format("?%s", s))
            .orElse("");
    String urlIdPath = String.format("/%d", id);
    String url = geeklistReadEndpoint + urlIdPath + urlParams;
    return webClient.get()
            .uri(url)
            .accept(MediaType.APPLICATION_XML)
            .acceptCharset(StandardCharsets.UTF_8)
            .exchangeToMono(c -> c.toEntity(Geeklist.class))
            .block();
  }

}
