package li.naska.bgg.repository;

import com.boardgamegeek.thing.Things;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BggThingsService {

  @Value("${bgg.endpoints.thing.read}")
  private String thingReadEndpoint;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Things> getThings(String commaSeparatedIds, Map<String, String> extraParams) {
    String urlParams = String.format("?id=%s", commaSeparatedIds) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = thingReadEndpoint + urlParams;
    return restTemplate.getForEntity(url, Things.class);
  }

}
