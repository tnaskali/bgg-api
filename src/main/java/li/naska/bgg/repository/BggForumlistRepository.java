package li.naska.bgg.repository;

import li.naska.bgg.repository.model.BggForumsParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggForumlistRepository {

  private final WebClient webClient;

  public BggForumlistRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.forumlist}") String forumListEndpoint) {
    this.webClient = builder.baseUrl(forumListEndpoint).build();
  }

  public Mono<String> getForums(BggForumsParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(parameters.toMultiValueMap())
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class);
  }

}
