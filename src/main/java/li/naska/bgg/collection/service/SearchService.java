package li.naska.bgg.collection.service;

import com.boardgamegeek.xmlapi2.search.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {

  private static final String BGG_URL = "https://www.boardgamegeek.com/xmlapi2";

  private static final String SEARCH_ENDPOINT_PATH = "/search";

  private static final String SEARCH_ENDPOINT_URL = BGG_URL + SEARCH_ENDPOINT_PATH;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Items> getItems(String query, Map<String, String> extraParams) {
    String urlParams = String.format("?query=%s", query) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = SEARCH_ENDPOINT_URL + urlParams;
    return restTemplate.getForEntity(url, Items.class);
  }


}
