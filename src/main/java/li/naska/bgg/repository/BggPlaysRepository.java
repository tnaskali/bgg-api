package li.naska.bgg.repository;

import com.boardgamegeek.plays.Plays;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import li.naska.bgg.exception.BggAuthenticationRequiredError;
import li.naska.bgg.exception.BggBadRequestError;
import li.naska.bgg.repository.model.BggItemPlaysParameters;
import li.naska.bgg.repository.model.BggUserPlaysParameters;
import li.naska.bgg.repository.model.plays.BggPlay;
import li.naska.bgg.security.BggAuthenticationToken;
import li.naska.bgg.util.QueryParamFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Repository
public class BggPlaysRepository {

  private final WebClient readWebClient;

  private final WebClient writeWebClient;

  public BggPlaysRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.plays.read}") String playsReadEndpoint,
      @Value("${bgg.endpoints.plays.write}") String playsWriteEndpoint) {
    this.readWebClient = builder.baseUrl(playsReadEndpoint).build();
    this.writeWebClient = builder.baseUrl(playsWriteEndpoint)
        .filter((request, next) -> next.exchange(
            ClientRequest.from(request)
                .headers((headers) -> headers.set("Cookie", getAuthentication().buildBggRequestHeader()))
                .build()))
        .build();
  }

  private static BggAuthenticationToken getAuthentication() {
    return (BggAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
  }

  private static MultiValueMap<String, String> extractUserPlayParams(BggUserPlaysParameters params) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("username", params.getUsername());
    Optional.ofNullable(params.getId()).map(Object::toString).ifPresent(e -> map.set("id", e));
    Optional.ofNullable(params.getType()).map(QueryParamFunctions.BGG_OBJECT_TYPE_FUNCTION).ifPresent(e -> map.set("type", e));
    Optional.ofNullable(params.getMindate()).map(QueryParamFunctions.BGG_LOCALDATE_FUNCTION).ifPresent(e -> map.set("mindate", e));
    Optional.ofNullable(params.getMaxdate()).map(QueryParamFunctions.BGG_LOCALDATE_FUNCTION).ifPresent(e -> map.set("maxdate", e));
    Optional.ofNullable(params.getSubtype()).map(QueryParamFunctions.BGG_OBJECT_SUBTYPE_FUNCTION).ifPresent(e -> map.set("subtype", e));
    Optional.ofNullable(params.getPage()).map(Object::toString).ifPresent(e -> map.set("page", e));
    return map;
  }

  private static MultiValueMap<String, String> extractItemPlayParams(BggItemPlaysParameters params) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("id", params.getId().toString());
    map.set("type", params.getType().value());
    Optional.ofNullable(params.getUsername()).ifPresent(e -> map.set("username", e));
    Optional.ofNullable(params.getMindate()).map(QueryParamFunctions.BGG_LOCALDATE_FUNCTION).ifPresent(e -> map.set("mindate", e));
    Optional.ofNullable(params.getMaxdate()).map(QueryParamFunctions.BGG_LOCALDATE_FUNCTION).ifPresent(e -> map.set("maxdate", e));
    Optional.ofNullable(params.getSubtype()).map(QueryParamFunctions.BGG_OBJECT_SUBTYPE_FUNCTION).ifPresent(e -> map.set("subtype", e));
    Optional.ofNullable(params.getPage()).map(Object::toString).ifPresent(e -> map.set("page", e));
    return map;
  }

  public Mono<Plays> getPlays(BggUserPlaysParameters parameters) {
    return readWebClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(extractUserPlayParams(parameters))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Plays.class);
  }

  public Mono<Plays> getPlays(BggItemPlaysParameters parameters) {
    return readWebClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(extractItemPlayParams(parameters))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Plays.class);
  }

  public Mono<String> savePlay(String username, Integer id, BggPlay play) {
    ObjectNode node = new ObjectMapper().valueToTree(play);
    node.put("ajax", 1);
    node.put("action", "save");
    if (id != null) {
      node.put("playid", id);
      node.put("version", 2);
    }
    return writeWebClient
        .post()
        .uri(uriBuilder -> uriBuilder
            .queryParam("username", username)
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(node)
        .retrieve()
        .bodyToMono(Map.class)
        .flatMap(b -> {
          String playId = (String) b.get("playid");
          if (playId != null) {
            return Mono.just(playId);
          }
          String error = (String) b.get("error");
          if ("you must login to save plays".equals(error)) {
            return Mono.error(new BggAuthenticationRequiredError(error));
          } else {
            return Mono.error(new BggBadRequestError(Optional.ofNullable(error).orElse("unknown")));
          }
        });
  }

  public Mono<String> deletePlay(String username, Integer playId) {
    ObjectNode node = new ObjectMapper().createObjectNode();
    node.put("action", "delete");
    node.put("finalize", "1");
    node.put("playid", playId.toString());
    return writeWebClient
        .post()
        .uri(uriBuilder -> uriBuilder
            .queryParam("username", username)
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(node)
        .retrieve()
        .onStatus(
            status -> status != HttpStatus.FOUND,
            response -> Mono.empty())
        .bodyToMono(String.class);
  }

}
