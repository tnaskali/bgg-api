package li.naska.bgg.service;

import com.boardgamegeek.search.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BggSearchService {

  @Value("${bgg.endpoints.search.read}")
  private String searchReadEndpoint;

  @Autowired
  public WebClient webClient;

  public ResponseEntity<Results> getItems(String query, Map<String, String> extraParams) {
    String urlParams = String.format("?query=%s", query) + extraParams
            .entrySet()
            .stream()
            .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
            .collect(Collectors.joining());
    String url = searchReadEndpoint + urlParams;
    return webClient.get()
            .uri(url)
            .accept(MediaType.APPLICATION_XML)
            .acceptCharset(StandardCharsets.UTF_8)
            .exchangeToMono(c -> c.toEntity(Results.class))
            .block();
  }


}
