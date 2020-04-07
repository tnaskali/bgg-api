package li.naska.bgg.collection.service;

import com.boardgamegeek.xmlapi2.thing.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ThingsService {

  private static final String BGG_URL = "https://www.boardgamegeek.com/xmlapi2";

  private static final String THINGS_ENDPOINT_PATH = "/thing";

  private static final String THINGS_ENDPOINT_URL = BGG_URL + THINGS_ENDPOINT_PATH;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Items> getThings(String commaSeparatedIds, Map<String, String> extraParams) {
    String urlParams = String.format("?id=%s", commaSeparatedIds) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = THINGS_ENDPOINT_URL + urlParams;
    return restTemplate.getForEntity(url, Items.class);
  }

}
