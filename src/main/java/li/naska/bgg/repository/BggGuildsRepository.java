package li.naska.bgg.repository;

import com.boardgamegeek.guild.Guild;
import li.naska.bgg.repository.model.BggGuildParameters;
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
public class BggGuildsRepository {

  private final WebClient webClient;

  public BggGuildsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.guild.read}") String guildReadEndpoint) {
    this.webClient = builder.baseUrl(guildReadEndpoint).build();
  }

  private static MultiValueMap<String, String> extractFamiliesParams(BggGuildParameters params) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("id", params.getId().toString());
    Optional.ofNullable(params.getMembers()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("members", e));
    Optional.ofNullable(params.getSort()).map(QueryParamFunctions.BGG_SORT_TYPE_FUNCTION).ifPresent(e -> map.set("sort", e));
    Optional.ofNullable(params.getPage()).map(Object::toString).ifPresent(e -> map.set("page", e));
    return map;
  }

  public Mono<Guild> getGuild(BggGuildParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(extractFamiliesParams(parameters))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Guild.class);
  }

}
