package li.naska.bgg.service;

import com.boardgamegeek.collection.Collection;
import com.boardgamegeek.enums.ObjectSubtype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class CollectionService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CollectionService.class);

  private static final String COLLECTION_ENDPOINT_PATH = "/collection";

  @Value("${bgg.api.v2.baseurl-bgs}")
  private String baseurl;

  @Autowired
  public RestTemplate restTemplate;

  private static void waitFor(int millis) {
    try {
      Thread.sleep(millis);
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
    final String url = baseurl + COLLECTION_ENDPOINT_PATH + urlParams;

    // BGG might queue the request
    return tryResponse(() -> restTemplate.getForEntity(url, Collection.class), 1000, 4);
  }

  private ResponseEntity<Collection> tryResponse(Supplier<ResponseEntity<Collection>> responseSupplier, int waitMillis, int maxRetries) {
    ResponseEntity<Collection> response = responseSupplier.get();
    if (response.getStatusCode() == HttpStatus.ACCEPTED) {
      if (maxRetries > 0) {
        waitFor(waitMillis);
        return tryResponse(responseSupplier, waitMillis * 2, maxRetries - 1);
      } else {
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
      }
    } else {
      return response;
    }
  }

}
