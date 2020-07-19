package li.naska.bgg.repository;

import com.boardgamegeek.enums.ObjectType;
import com.boardgamegeek.forumlist.Forums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BggForumListsService {

  @Value("${bgg.endpoints.forumlist.read}")
  private String forumlistReadEndpoint;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Forums> getForums(Integer id, ObjectType type) {
    String urlParams = String.format("?id=%d&type=%s", id, type.value());
    String url = forumlistReadEndpoint + urlParams;
    return restTemplate.getForEntity(url, Forums.class);
  }

}
