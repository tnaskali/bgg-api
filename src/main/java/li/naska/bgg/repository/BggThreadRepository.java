package li.naska.bgg.repository;

import li.naska.bgg.repository.model.BggThreadParameters;
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
public class BggThreadRepository {

  private final WebClient webClient;

  public BggThreadRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.thread}") String threadEndpoint) {
    this.webClient = builder.baseUrl(threadEndpoint).build();
  }

  public Mono<String> getThread(BggThreadParameters parameters) {
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
          if (body.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?><error message='Thread Not Found' />")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found");
          }
        });
  }

}
