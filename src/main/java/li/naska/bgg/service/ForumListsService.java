package li.naska.bgg.service;

import com.boardgamegeek.forumlist.ForumType;
import com.boardgamegeek.forumlist.Forums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ForumListsService {

  private static final String FORUMLISTS_ENDPOINT_PATH = "/forumlist";

  @Value("${bgg.api.v2.baseurl-bgs}")
  private String baseurl;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Forums> getForums(Integer id, ForumType type) {
    String urlParams = String.format("?id=%d&type=%s", id, type.value());
    String url = baseurl + FORUMLISTS_ENDPOINT_PATH + urlParams;
    return restTemplate.getForEntity(url, Forums.class);
  }

}
