package li.naska.bgg.service;

import com.boardgamegeek.geeklist.Geeklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GeeklistsService {

  private static final String GEEKLISTS_ENDPOINT_PATH = "/geeklist";

  @Value("${bgg.api.v1.baseurl-bgs}")
  private String baseurl;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Geeklist> getGeeklist(Integer id, Map<String, String> extraParams) {
    String urlParams = extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("%s=%s", entry.getKey(), entry.getValue()))
        .reduce((x, y) -> String.format("%s&%s", x, y))
        .map(s -> String.format("?%s", s))
        .orElse("");
    String urlIdPath = String.format("/%d", id);
    String url = baseurl + GEEKLISTS_ENDPOINT_PATH + urlIdPath + urlParams;
    return restTemplate.getForEntity(url, Geeklist.class);
  }

}
