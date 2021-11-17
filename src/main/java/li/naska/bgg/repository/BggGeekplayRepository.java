package li.naska.bgg.repository;

import com.boardgamegeek.geekplay.Play;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import li.naska.bgg.security.BggAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class BggGeekplayRepository {

  private final WebClient geekplayWebClient;

  public BggGeekplayRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.geekplay}") String geekplayEndpoint) {
    this.geekplayWebClient = builder.baseUrl(geekplayEndpoint).build();
  }

  private static ResponseStatusException unauthorizedWrongUser() {
    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong user");
  }

  private Mono<BggAuthenticationToken> authentication() {
    return ReactiveSecurityContextHolder.getContext().map(
        context -> (BggAuthenticationToken) context.getAuthentication()
    );
  }

  public Mono<String> savePlay(String username, Integer id, Play play) {
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
            .doOnNext(entity -> {
                  if (MediaType.TEXT_HTML.equalsTypeAndSubtype(entity.getHeaders().getContentType())) {
                    Matcher matcher = Pattern.compile("<div class='messagebox error'>\\s*(.+)\\s*</div>").matcher(entity.getBody());
                    if (matcher.find()) {
                      String error = matcher.group(1);
                      if ("Play does not exist.".equals(error)) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Play does not exist");
                      } else if ("You are not permitted to edit this play.".equals(error)) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Operation not allowed");
                      } else {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error);
                      }
                    } else {
                      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "BGG Service error");
                    }
                  }
                }
            )
            .map(HttpEntity::getBody)
            .doOnNext(body -> {
              Configuration conf = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
              String error = JsonPath.using(conf).parse(body).read("$['error']", String.class);
              if (error != null) {
                if ("You must login to save plays".equals(error)) {
                  throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Authentication required");
                } else if ("Invalid item. Play not saved.".equals(error)) {
                  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid item");
                } else if ("Invalid action".equals(error)) {
                  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action");
                } else {
                  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
                }
              }
            }));
  }

  public Mono<String> deletePlay(String username, Integer playId) {
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
            .doOnNext(entity -> {
                  if (MediaType.TEXT_HTML.equalsTypeAndSubtype(entity.getHeaders().getContentType())) {
                    Matcher matcher = Pattern.compile("<div class='messagebox error'>\\s*(.+)\\s*</div>").matcher(Objects.requireNonNull(entity.getBody()));
                    if (matcher.find()) {
                      String error = matcher.group(1);
                      if ("Play does not exist.".equals(error)) {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Play does not exist");
                      } else if ("You can't delete this play".equals(error)) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You can't delete this play");
                      } else if ("Invalid action".equals(error)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action");
                      } else {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error);
                      }
                    } else {
                      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "BGG Service error");
                    }
                  }
                }
            )
            .map(HttpEntity::getBody)
            .doOnNext(body -> {
              if (!"{\"success\":true}".equals(body)) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting play");
              }
            }));
  }

}
