package li.naska.bgg.repository;

import li.naska.bgg.repository.model.BggUserParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggUserRepository {

  private final WebClient webClient;

  public BggUserRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.user}") String userEndpoint) {
    this.webClient = builder.baseUrl(userEndpoint).build();
  }

  public Mono<String> getUser(BggUserParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(parameters.toMultiValueMap())
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class)
        .doOnNext(body -> {
          if (body.startsWith("<?xml version=\"1.0\" encoding=\"utf-8\"?><user id=\"\"")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
          } else if (body.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?>\t<div class='messagebox error'>\n\t\tinvalid Get list data\n\t</div>")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
          }
        });

  }

}
