package li.naska.bgg.service;

import com.boardgamegeek.forum.Forum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ForumsService {

  private static final String FORUMS_ENDPOINT_PATH = "/forum";

  @Value("${bgg.api.v2.baseurl-bgs}")
  private String baseurl;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Forum> getForum(Integer id, Map<String, String> extraParams) {
    String urlParams = String.format("?id=%d", id) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = baseurl + FORUMS_ENDPOINT_PATH + urlParams;
    return restTemplate.getForEntity(url, Forum.class);
  }

}
