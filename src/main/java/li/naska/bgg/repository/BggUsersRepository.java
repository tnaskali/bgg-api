package li.naska.bgg.repository;

import com.boardgamegeek.user.User;
import li.naska.bgg.repository.model.BggUserParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggUsersRepository {

  private final WebClient webClient;

  public BggUsersRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.user}") String userEndpoint) {
    this.webClient = builder.baseUrl(userEndpoint).build();
  }

  public Mono<User> getUser(BggUserParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(parameters.toMultiValueMap())
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(User.class);
  }

}
