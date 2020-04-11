package li.naska.bgg.service;

import com.boardgamegeek.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsersService {

  private static final String BASE_URL_V2 = "https://www.boardgamegeek.com/xmlapi2";

  private static final String USERS_ENDPOINT_PATH = "/user";

  private static final String USERS_ENDPOINT_URL = BASE_URL_V2 + USERS_ENDPOINT_PATH;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<User> getUser(String username, Map<String, String> extraParams) {
    String urlParams = String.format("?name=%s", username) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = USERS_ENDPOINT_URL + urlParams;
    return restTemplate.getForEntity(url, User.class);
  }

}
