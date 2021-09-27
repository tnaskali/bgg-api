package li.naska.bgg.repository;

import com.boardgamegeek.forumlist.Forums;
import li.naska.bgg.repository.model.BggForumsParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggForumListsRepository {

  private final WebClient webClient;

  public BggForumListsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.forumlist.read}") String forumlistReadEndpoint) {
    this.webClient = builder.baseUrl(forumlistReadEndpoint).build();
  }

  private static MultiValueMap<String, String> extractThreadsParams(BggForumsParameters params) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("id", params.getId().toString());
    map.set("type", params.getType().value());
    return map;
  }

  public Mono<Forums> getForums(BggForumsParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(extractThreadsParams(parameters))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Forums.class);
  }

}
