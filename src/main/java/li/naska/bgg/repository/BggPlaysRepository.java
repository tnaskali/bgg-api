package li.naska.bgg.repository;

import com.boardgamegeek.plays.Plays;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import li.naska.bgg.repository.model.BggItemPlaysParameters;
import li.naska.bgg.repository.model.BggUserPlaysParameters;
import li.naska.bgg.repository.model.plays.BggPlay;
import li.naska.bgg.security.BggAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class BggPlaysRepository {

  private final WebClient playsWebClient;

  private final WebClient geekplayWebClient;

  public BggPlaysRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.plays}") String playsEndpoint,
      @Value("${bgg.endpoints.geekplay}") String geekplayEndpoint) {
    this.playsWebClient = builder.baseUrl(playsEndpoint).build();
    this.geekplayWebClient = builder.baseUrl(geekplayEndpoint).build();
  }

  private static ResponseStatusException unauthorizedWrongUser() {
    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong user");
  }

  private static ResponseStatusException fromMapBody(Map<String, Object> body) {
    String error = (String) body.get("error");
    if ("You must login to save plays".equals(error)) {
      return new ResponseStatusException(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED, "You must login to save plays");
    } else {
      return new ResponseStatusException(HttpStatus.BAD_REQUEST, Optional.ofNullable(error).orElse("Unknown remote error"));
    }
  }

  private static ResponseStatusException fromHtmlBody(String error) {
    if (error == null) {
      return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown remote error");
    }
    Matcher matcher = Pattern.compile("<div class='messagebox error'>\\s*(.+)\\s*</div>").matcher(error);
    String statusText = matcher.find() ? matcher.group(1) : "Unknown remote error";
    if ("Play does not exist.".equals(statusText)) {
      return new ResponseStatusException(HttpStatus.NOT_FOUND, "Play does not exist");
    } else if ("Invalid action".equals(statusText)) {
      return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action");
    } else if ("You are not permitted to edit this play.".equals(statusText)) {
      return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not permitted to edit this play");
    } else if ("You can't delete this play".equals(statusText)) {
      return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You can't delete this play");
    } else {
      return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, statusText != null ? statusText : "Unknown remote error");
    }
  }

  private Mono<BggAuthenticationToken> authentication() {
    return ReactiveSecurityContextHolder.getContext().map(
        context -> (BggAuthenticationToken) context.getAuthentication()
    );
  }

  public Mono<Plays> getPlays(BggUserPlaysParameters parameters) {
    return playsWebClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(parameters.toMultiValueMap())
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Plays.class);
  }

  public Mono<Plays> getPlays(BggItemPlaysParameters parameters) {
    return playsWebClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(parameters.toMultiValueMap())
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Plays.class);
  }

  public Mono<Map<String, Object>> savePlay(String username, Integer id, BggPlay play) {
    ObjectNode requestBody = new ObjectMapper().valueToTree(play);
    requestBody.put("ajax", 1);
    requestBody.put("action", "save");
    if (id != null) {
      requestBody.put("playid", id);
      requestBody.put("version", 2);
    }
    return authentication()
        .flatMap(auth -> auth.getPrincipal().equals(username)
            ? Mono.just(auth)
            : Mono.error(unauthorizedWrongUser()))
        .flatMap(auth -> geekplayWebClient
            .post()
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Cookie", auth.buildBggRequestHeader())
            .bodyValue(requestBody)
            .retrieve()
            .toEntity(String.class)
            .flatMap(
                entity -> MediaType.TEXT_HTML.equalsTypeAndSubtype(entity.getHeaders().getContentType())
                    ? Mono.error(fromHtmlBody(entity.getBody()))
                    : Mono.just(Objects.requireNonNull(entity.getBody()))))
        .map(responseBody -> {
          try {
            return new ObjectMapper().readValue(responseBody, new TypeReference<Map<String, Object>>() {
            });
          } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
          }
        })
        .flatMap(responseBody -> Optional.of(responseBody.get("playid"))
            .map(element -> Mono.just(responseBody))
            .orElseGet(() -> Mono.error(fromMapBody(responseBody))));
  }

  public Mono<Map<String, Object>> deletePlay(String username, Integer playId) {
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("ajax", 1);
    requestBody.put("action", "delete");
    requestBody.put("finalize", "1");
    requestBody.put("playid", playId.toString());
    return authentication()
        .flatMap(auth -> auth.getPrincipal().equals(username)
            ? Mono.just(auth)
            : Mono.error(unauthorizedWrongUser()))
        .flatMap(auth -> geekplayWebClient
            .post()
            .accept(MediaType.APPLICATION_JSON)
            .acceptCharset(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Cookie", auth.buildBggRequestHeader())
            .bodyValue(requestBody)
            .retrieve()
            .toEntity(String.class)
            .flatMap(
                entity -> MediaType.TEXT_HTML.equalsTypeAndSubtype(entity.getHeaders().getContentType())
                    ? Mono.error(fromHtmlBody(entity.getBody()))
                    : Mono.just(Objects.requireNonNull(entity.getBody()))))
        .map(responseBody -> {
          Map<String, Object> play = new HashMap<>();
          play.put("playid", Integer.parseInt(responseBody));
          return play;
        });
  }

}
