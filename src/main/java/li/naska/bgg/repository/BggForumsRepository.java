package li.naska.bgg.repository;

import com.boardgamegeek.forum.Forum;
import li.naska.bgg.repository.model.BggForumParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggForumsRepository {

  private final WebClient webClient;

  public BggForumsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.forum}") String forumEndpoint) {
    this.webClient = builder.baseUrl(forumEndpoint).build();
  }

  public Mono<Forum> getForum(BggForumParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(parameters.toMultiValueMap())
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve().bodyToMono(Forum.class);
  }

}
