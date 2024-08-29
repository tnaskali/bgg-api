package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.repository.model.BggPlaysV2QueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggPlaysV2Repository {

  private final WebClient webClient;

  public BggPlaysV2Repository(
      @Autowired WebClient.Builder builder, @Value("${bgg.endpoints.v2.plays}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<String> getPlays(BggPlaysV2QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class)
        .doOnNext(body -> {
          if (body.equals(
              "<?xml version=\"1.0\" encoding=\"utf-8\"?><error message='Not Found'/>")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Play not found");
          }
        });
  }
}
