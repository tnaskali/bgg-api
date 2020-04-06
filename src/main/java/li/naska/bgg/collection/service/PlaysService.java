package li.naska.bgg.collection.service;

import com.boardgamegeek.xmlapi2.items.ItemSubtypeEnum;
import com.boardgamegeek.xmlapi2.items.Items;
import com.boardgamegeek.xmlapi2.plays.Plays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlaysService {

  private static final String BASE_URL_V2 = "https://www.boardgamegeek.com/xmlapi2";

  private static final String PLAYS_ENDPOINT_PATH = "/plays";

  private static final String PLAYS_ENDPOINT_URL = BASE_URL_V2 + PLAYS_ENDPOINT_PATH;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Plays> getPlays(String username, Map<String, String> extraParams) {
    String urlParams = String.format("?username=%s", username) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .reduce(String::concat);
    String url = PLAYS_ENDPOINT_URL + urlParams;
    return restTemplate.getForEntity(url, Plays.class);
  }

}
