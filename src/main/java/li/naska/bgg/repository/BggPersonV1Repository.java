package li.naska.bgg.repository;

import li.naska.bgg.exception.BggConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class BggPersonV1Repository {

  private final WebClient webClient;

  public BggPersonV1Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v1.person}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<String> getPersons(Set<Integer> ids) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{ids}")
            .build(ids.stream().map(Objects::toString).collect(Collectors.joining(","))))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class)
        .onErrorMap(IOException.class, ioe -> new BggConnectionException())
        .retryWhen(
            Retry.max(3)
                .filter(throwable -> throwable instanceof BggConnectionException));
  }

}
