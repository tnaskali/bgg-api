package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.repository.model.BggUserV2QueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggUserV2Repository {

  private final WebClient webClient;

  public BggUserV2Repository(
      @Value("${bgg.endpoints.v2.user}") String endpoint, WebClient.Builder builder) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<String> getUser(BggUserV2QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class)
        .doOnNext(body -> {
          if (body.startsWith("<?xml version=\"1.0\" encoding=\"utf-8\"?><user id=\"\"")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
          } else if (body.equals(
              "<?xml version=\"1.0\" encoding=\"utf-8\"?>\t<div class='messagebox error'>\n\t\tinvalid Get list data\n\t</div>")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
          }
        });
  }
}
