package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.repository.model.BggThreadV2QueryParams;
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
public class BggThreadV2Repository {

  private final WebClient webClient;

  public BggThreadV2Repository(
      @Autowired WebClient.Builder builder, @Value("${bgg.endpoints.v2.thread}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<String> getThread(BggThreadV2QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class)
        .doOnNext(
            body -> {
              if (body.equals(
                  "<?xml version=\"1.0\" encoding=\"utf-8\"?><error message='Thread Not Found' />")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found");
              }
            });
  }
}
