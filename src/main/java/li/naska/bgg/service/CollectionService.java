package li.naska.bgg.service;

import com.boardgamegeek.collection.Collection;
import com.boardgamegeek.enums.ObjectSubtype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CollectionService {

  private static final String COLLECTION_ENDPOINT_PATH = "/collection";

  @Value("${bgg.api.v2.baseurl-bgs}")
  private String baseurl;

  @Autowired
  public RestTemplate restTemplate;

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
    String url = baseurl + COLLECTION_ENDPOINT_PATH + urlParams;
    return restTemplate.getForEntity(url, Collection.class);
  }

}
