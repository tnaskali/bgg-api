package li.naska.bgg.service;

import com.boardgamegeek.hot.ItemTypeEnum;
import com.boardgamegeek.hot.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HotItemsService {

  private static final String BGG_URL = "https://www.boardgamegeek.com/xmlapi2";

  private static final String HOTITEMS_ENDPOINT_PATH = "/hot";

  private static final String HOTITEMS_ENDPOINT_URL = BGG_URL + HOTITEMS_ENDPOINT_PATH;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Items> getItems(ItemTypeEnum type) {
    String url = HOTITEMS_ENDPOINT_URL + String.format("?type=%s", type.value());
    return restTemplate.getForEntity(url, Items.class);
  }

}
