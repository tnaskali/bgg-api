package li.naska.bgg.service;

import com.boardgamegeek.guild.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BggGuildsService {

  @Value("${bgg.endpoints.guild.read}")
  private String guildReadEndpoint;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Guild> getGuild(Integer id, Map<String, String> extraParams) {
    String urlParams = String.format("?id=%d", id) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = guildReadEndpoint + urlParams;
    return restTemplate.getForEntity(url, Guild.class);
  }

}
