package li.naska.bgg.service;

import com.boardgamegeek.guild.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GuildsService {

  private static final String BGG_URL = "https://www.boardgamegeek.com/xmlapi2";

  private static final String GUILDS_ENDPOINT_PATH = "/guild";

  private static final String GUILDS_ENDPOINT_URL = BGG_URL + GUILDS_ENDPOINT_PATH;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Guild> getGuild(Integer id, Map<String, String> extraParams) {
    String urlParams = String.format("?id=%d", id) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = GUILDS_ENDPOINT_URL + urlParams;
    return restTemplate.getForEntity(url, Guild.class);
  }

}
