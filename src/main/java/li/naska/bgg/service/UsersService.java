package li.naska.bgg.service;

import com.boardgamegeek.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsersService {

  private static final String USERS_ENDPOINT_PATH = "/user";

  @Value("${bgg.api.v2.baseurl-bgs}")
  private String baseurl;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<User> getUser(String username, Map<String, String> extraParams) {
    String urlParams = String.format("?name=%s", username) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = baseurl + USERS_ENDPOINT_PATH + urlParams;
    return restTemplate.getForEntity(url, User.class);
  }

}
