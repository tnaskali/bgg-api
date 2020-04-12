package li.naska.bgg.service;

import com.boardgamegeek.search.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {

  private static final String SEARCH_ENDPOINT_PATH = "/search";

  @Value("${bgg.api.v2.baseurl-bgs}")
  private String baseurl;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Results> getItems(String query, Map<String, String> extraParams) {
    String urlParams = String.format("?query=%s", query) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = baseurl + SEARCH_ENDPOINT_PATH + urlParams;
    return restTemplate.getForEntity(url, Results.class);
  }


}