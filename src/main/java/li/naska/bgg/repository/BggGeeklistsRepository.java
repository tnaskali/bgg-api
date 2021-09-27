package li.naska.bgg.repository;

import com.boardgamegeek.geeklist.Geeklist;
import li.naska.bgg.repository.model.BggGeeklistsParameters;
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
public class BggGeeklistsRepository {

  private final WebClient webClient;

  public BggGeeklistsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.geeklist.read}") String geeklistReadEndpoint) {
    this.webClient = builder.baseUrl(geeklistReadEndpoint).build();
  }

  private static MultiValueMap<String, String> extractForumParams(BggGeeklistsParameters params) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    Optional.ofNullable(params.getComments()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("comments", e));
    return map;
  }

  public Mono<Geeklist> getGeeklist(BggGeeklistsParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(extractForumParams(parameters))
            .path("/{id}")
            .build(parameters.getId()))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Geeklist.class);
  }

}
