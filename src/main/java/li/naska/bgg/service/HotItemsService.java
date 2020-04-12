package li.naska.bgg.service;

import com.boardgamegeek.hot.HotItemType;
import com.boardgamegeek.hot.HotItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HotItemsService {

  private static final String HOTITEMS_ENDPOINT_PATH = "/hot";

  @Value("${bgg.api.v2.baseurl-bgs}")
  private String baseurl;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<HotItems> getItems(HotItemType type) {
    String url = baseurl + HOTITEMS_ENDPOINT_PATH + String.format("?type=%s", type.value());
    return restTemplate.getForEntity(url, HotItems.class);
  }

}
