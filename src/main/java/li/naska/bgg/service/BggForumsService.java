package li.naska.bgg.service;

import com.boardgamegeek.forum.Forum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BggForumsService {

  @Value("${bgg.endpoints.forum.read}")
  private String forumReadEndpoint;

    @Autowired
    public WebClient webClient;

  public ResponseEntity<Forum> getForum(Integer id, Map<String, String> extraParams) {
      String urlParams = String.format("?id=%d", id) + extraParams
              .entrySet()
              .stream()
              .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
              .collect(Collectors.joining());
      String url = forumReadEndpoint + urlParams;
      return webClient.get()
              .uri(url)
              .accept(MediaType.APPLICATION_XML)
              .acceptCharset(StandardCharsets.UTF_8)
              .exchangeToMono(c -> c.toEntity(Forum.class))
              .block();
  }

}
