package li.naska.bgg.repository;

import com.boardgamegeek.forum.Forum;
import li.naska.bgg.repository.model.BggForumParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

@Repository
public class BggForumsRepository {

  private final WebClient webClient;

  public BggForumsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.forum.read}") String forumReadEndpoint) {
    this.webClient = builder.baseUrl(forumReadEndpoint).build();
  }

  private static MultiValueMap<String, String> extractForumParams(BggForumParameters params) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("id", params.getId().toString());
    Optional.ofNullable(params.getPage()).map(Objects::toString).ifPresent(e -> map.set("page", e));
    return map;
  }

  public Mono<Forum> getForum(BggForumParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(extractForumParams(parameters))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve().bodyToMono(Forum.class);
  }

}
