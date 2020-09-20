package li.naska.bgg.service;

import com.boardgamegeek.plays.Plays;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import li.naska.bgg.exception.BggAuthenticationRequiredError;
import li.naska.bgg.exception.BggBadRequestError;
import li.naska.bgg.exception.BggResourceNotFoundError;
import li.naska.bgg.exception.BggResourceNotOwnedError;
import li.naska.bgg.security.BggAuthenticationToken;
import li.naska.bgg.service.model.plays.BggPlay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Service
public class BggPlaysService {

  @Autowired
  private RestTemplate restTemplate;
  @Value("${bgg.endpoints.plays.read}")
  private String playsReadEndpoint;
  @Value("${bgg.endpoints.plays.write}")
  private String playsWriteEndpoint;

  private static BggAuthenticationToken getAuthentication() {
    return (BggAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
  }

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

  public Integer savePlay(String username, Integer id, BggPlay play) {
    BggAuthenticationToken auth = getAuthentication();
    if (!auth.getPrincipal().equals(username)) {
      throw new BggResourceNotOwnedError("resource belongs to another user");
    }
    ObjectNode node = new ObjectMapper().valueToTree(play);
    node.put("ajax", 1);
    node.put("action", "save");
    if (id != null) {
      node.put("playid", id);
      node.put("version", 2);
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
    headers.set("Cookie", auth.buildBggRequestHeader());
    HttpEntity<ObjectNode> request = new HttpEntity<>(node, headers);
    ResponseEntity<String> response = restTemplate.postForEntity(playsWriteEndpoint, request, String.class);
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

  public void deletePlay(String username, Integer playId) {
    BggAuthenticationToken auth = getAuthentication();
    if (!auth.getPrincipal().equals(username)) {
      throw new BggResourceNotOwnedError("resource belongs to another user");
    }
    ObjectNode node = new ObjectMapper().createObjectNode();
    node.put("action", "delete");
    node.put("finalize", "1");
    node.put("playid", playId.toString());
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
    requestHeaders.setAccept(singletonList(MediaType.APPLICATION_JSON));
    requestHeaders.set("Cookie", auth.buildBggRequestHeader());
    HttpEntity<ObjectNode> request = new HttpEntity<>(node, requestHeaders);
    ResponseEntity<String> response = restTemplate.postForEntity(playsWriteEndpoint, request, String.class);
    if (response.getStatusCode() != HttpStatus.FOUND) {
      throw new BggResourceNotFoundError("play not found");
    }
  }

}
