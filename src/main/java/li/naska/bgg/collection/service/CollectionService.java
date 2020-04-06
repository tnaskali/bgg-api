package li.naska.bgg.collection.service;

import com.boardgamegeek.xmlapi2.items.ItemSubtypeEnum;
import com.boardgamegeek.xmlapi2.items.ItemTypeEnum;
import com.boardgamegeek.xmlapi2.items.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CollectionService {

  private static final String BOARDGAMES_URL = "https://www.boardgamegeek.com/xmlapi2";

  private static final String COLLECTION_ENDPOINT_PATH = "/collection";

  private static final String COLLECTION_ENDPOINT_URL = BOARDGAMES_URL + COLLECTION_ENDPOINT_PATH;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Items> getItems(String username, Map<String, String> extraParams) {
    if (!extraParams.containsKey("subtype") || ItemSubtypeEnum.BOARDGAME.equals(extraParams.get("subtype"))) {
      // bug in the BBG XML API
      extraParams.put("excludesubtype", ItemSubtypeEnum.BOARDGAMEEXPANSION.value());
    }
    String urlParams = String.format("?username=%s", username) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .reduce(String::concat)
        .get();
    String url = COLLECTION_ENDPOINT_URL + urlParams;
    return restTemplate.getForEntity(url, Items.class);
  }

}
