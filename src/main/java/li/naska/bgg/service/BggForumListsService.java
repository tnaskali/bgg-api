package li.naska.bgg.service;

import com.boardgamegeek.enums.ObjectType;
import com.boardgamegeek.forumlist.Forums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;

@Service
public class BggForumListsService {

  @Value("${bgg.endpoints.forumlist.read}")
  private String forumlistReadEndpoint;

    @Autowired
    public WebClient webClient;

  public ResponseEntity<Forums> getForums(Integer id, ObjectType type) {
      String urlParams = String.format("?id=%d&type=%s", id, type.value());
      String url = forumlistReadEndpoint + urlParams;
      return webClient.get()
              .uri(url)
              .accept(MediaType.APPLICATION_XML)
              .acceptCharset(StandardCharsets.UTF_8)
              .exchangeToMono(c -> c.toEntity(Forums.class))
              .block();
  }

}
