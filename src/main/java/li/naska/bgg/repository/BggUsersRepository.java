package li.naska.bgg.repository;

import com.boardgamegeek.user.User;
import li.naska.bgg.repository.model.BggUserParameters;
import li.naska.bgg.util.QueryParamFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Repository
public class BggUsersRepository {

  private final WebClient webClient;

  public BggUsersRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.user.read}") String userReadEndpoint) {
    this.webClient = builder.baseUrl(userReadEndpoint).build();
  }

  private static MultiValueMap<String, String> extractUserParams(BggUserParameters params) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("name", params.getUsername());
    Optional.ofNullable(params.getBuddies()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("buddies", e));
    Optional.ofNullable(params.getGuilds()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("guilds", e));
    Optional.ofNullable(params.getHot()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("hot", e));
    Optional.ofNullable(params.getTop()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("top", e));
    Optional.ofNullable(params.getDomain()).map(QueryParamFunctions.BGG_DOMAIN_TYPE_FUNCTION).ifPresent(e -> map.set("domain", e));
    Optional.ofNullable(params.getPage()).map(Object::toString).ifPresent(e -> map.set("page", e));
    return map;
  }

  public Mono<User> getUser(BggUserParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(extractUserParams(parameters))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(User.class);
  }

}
