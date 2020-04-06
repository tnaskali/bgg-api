package li.naska.bgg.collection.service;

import com.boardgamegeek.xmlapi2.items.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BggGuildService {

  private String BASE_URL_V1 = "https://www.boardgamegeek.com/xmlapi";

  private String BASE_URL_V2 = "https://www.boardgamegeek.com/xmlapi2";

  private String bggHotEndpoint = "/hot?thing=%s";

  @Autowired
  public RestTemplate restTemplate;

  public Items getHot(String thingType) {
    String url = BASE_URL_V2 + String.format(bggHotEndpoint, thingType);
    ResponseEntity<Items> response = restTemplate.getForEntity(url, Items.class);
    return response.getBody();
  }

}
