package li.naska.bgg.service;

import com.boardgamegeek.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BggUsersService {

  @Value("${bgg.endpoints.user.read}")
  private String userReadEndpoint;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<User> getUser(String username, Map<String, String> extraParams) {
    String urlParams = String.format("?name=%s", username) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = userReadEndpoint + urlParams;
    ResponseEntity<User> result = restTemplate.getForEntity(url, User.class);
    if (result.getStatusCode() == HttpStatus.OK && result.getBody().getId() == null) {
      return ResponseEntity.notFound().build();
    } else {
      return result;
    }
  }

}
