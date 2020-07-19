package li.naska.bgg.repository;

import com.boardgamegeek.enums.HotItemType;
import com.boardgamegeek.hot.HotItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BggHotItemsService {

  @Value("${bgg.endpoints.hot.read}")
  private String hotReadEndpoint;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<HotItems> getItems(HotItemType type) {
    String url = hotReadEndpoint + String.format("?type=%s", type.value());
    return restTemplate.getForEntity(url, HotItems.class);
  }

}
