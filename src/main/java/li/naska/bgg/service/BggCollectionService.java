package li.naska.bgg.service;

import com.boardgamegeek.collection.Collection;
import com.boardgamegeek.enums.ObjectSubtype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class BggCollectionService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BggCollectionService.class);

  @Value("${bgg.endpoints.collection.read}")
  private String collectionReadEndpoint;

    @Autowired
    public WebClient webClient;

  private static void waitFor(int seconds) {
    try {
      TimeUnit.SECONDS.sleep(seconds);
    } catch (InterruptedException ie) {
      LOGGER.debug("sleep was interrupted", ie);
    }
  }

  public ResponseEntity<Collection> getItems(String username, Map<String, String> extraParams) {
    if (!extraParams.containsKey("subtype") || ObjectSubtype.BOARDGAME.value().equals(extraParams.get("subtype"))) {
        // bug in the BBG XML API
        extraParams.put("excludesubtype", ObjectSubtype.BOARDGAMEEXPANSION.value());
    }
      String urlParams = String.format("?username=%s", username) + extraParams
              .entrySet()
              .stream()
              .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
              .collect(Collectors.joining());
      final String url = collectionReadEndpoint + urlParams;

      return tryResponse(() -> webClient.get()
              .uri(url)
              .accept(MediaType.APPLICATION_XML)
              .acceptCharset(StandardCharsets.UTF_8)
              .exchangeToMono(c -> c.toEntity(Collection.class))
              .block(), 1, 4);
  }

  private ResponseEntity<Collection> tryResponse(Supplier<ResponseEntity<Collection>> responseSupplier, int waitSeconds, int maxRetries) {
    ResponseEntity<Collection> response = responseSupplier.get();
    if (response.getStatusCode() == HttpStatus.ACCEPTED) {
      // BGG might queue the request
      if (maxRetries > 0) {
        // Try after an inreasingly long delay to not stress the BGG server too much
        waitFor(waitSeconds);
        return tryResponse(responseSupplier, waitSeconds * 2, maxRetries - 1);
      } else {
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
      }
    } else {
      return response;
    }
  }

}
