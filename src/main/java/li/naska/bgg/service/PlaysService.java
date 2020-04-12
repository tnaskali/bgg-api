package li.naska.bgg.service;

import com.boardgamegeek.plays.Plays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlaysService {

  private static final String PLAYS_ENDPOINT_PATH = "/plays";

  @Value("${bgg.api.v2.baseurl-bgs}")
  private String baseurl;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Plays> getPlays(String username, Map<String, String> extraParams) {
    String urlParams = String.format("?username=%s", username) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = baseurl + PLAYS_ENDPOINT_PATH + urlParams;
    return restTemplate.getForEntity(url, Plays.class);
  }

  public ResponseEntity<Plays> getPlays(Integer id, String type, Map<String, String> extraParams) {
    String urlParams = String.format("?id=%d&type=%s", id, type) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = baseurl + PLAYS_ENDPOINT_PATH + urlParams;
    return restTemplate.getForEntity(url, Plays.class);
  }

}
