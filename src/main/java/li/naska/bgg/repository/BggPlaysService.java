package li.naska.bgg.repository;

import com.boardgamegeek.plays.Plays;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import li.naska.bgg.exception.BggAuthenticationRequiredError;
import li.naska.bgg.exception.BggBadRequestError;
import li.naska.bgg.repository.model.plays.BggPlayParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Service
public class BggPlaysService {

  @Autowired
  public RestTemplate restTemplate;
  @Value("${bgg.endpoints.plays.read}")
  private String playsReadEndpoint;
  @Value("${bgg.endpoints.plays.write}")
  private String playsWriteEnpoint;

  public ResponseEntity<Plays> getPlays(String username, Map<String, String> extraParams) {
    String urlParams = String.format("?username=%s", username) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = playsReadEndpoint + urlParams;
    return restTemplate.getForEntity(url, Plays.class);
  }

  public ResponseEntity<Plays> getPlays(Integer id, String type, Map<String, String> extraParams) {
    String urlParams = String.format("?id=%d&type=%s", id, type) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = playsReadEndpoint + urlParams;
    return restTemplate.getForEntity(url, Plays.class);
  }

  public Integer logPlay(String username, String sessionId, BggPlayParameters play) {
    ObjectNode node = new ObjectMapper().valueToTree(play);
    node.put("ajax", 1);
    node.put("action", "save");
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
    headers.set("Cookie", String.format("SessionID=%s; bggusername=%s", sessionId, username));
    HttpEntity<ObjectNode> request = new HttpEntity<>(node, headers);
    ResponseEntity<String> response = restTemplate.postForEntity(playsWriteEnpoint, request, String.class);
    Configuration conf = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
    DocumentContext documentContext = JsonPath.using(conf).parse(response.getBody());
    Integer playId = documentContext.read("$.playid", Integer.class);
    if (playId != null) {
      return playId;
    }
    String error = documentContext.read("$.error");
    if ("you must login to save plays".equals(error)) {
      throw new BggAuthenticationRequiredError(error);
    } else {
      throw new BggBadRequestError(Optional.ofNullable(error).orElse("unknown"));
    }
  }

  public ResponseEntity<String> logPlay(String username, String sessionId, String body) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Cookie", String.format("SessionID=%s; bggusername=%s", sessionId, username));
    HttpEntity<String> request = new HttpEntity<>(body, headers);
    return restTemplate.postForEntity(playsWriteEnpoint, request, String.class);
  }

}
