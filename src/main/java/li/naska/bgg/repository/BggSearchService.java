package li.naska.bgg.repository;

import com.boardgamegeek.search.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BggSearchService {

  @Value("${bgg.endpoints.search.read}")
  private String searchReadEndpoint;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Results> getItems(String query, Map<String, String> extraParams) {
    String urlParams = String.format("?query=%s", query) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = searchReadEndpoint + urlParams;
    return restTemplate.getForEntity(url, Results.class);
  }


}
