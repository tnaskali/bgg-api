package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class BggCompanyV1Repository {

  private final WebClient webClient;

  public BggCompanyV1Repository(
      @Value("${bgg.endpoints.v1.company}") String endpoint, WebClient.Builder builder) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<String> getCompanies(Set<Integer> ids) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{ids}")
            .build(ids.stream().map(Objects::toString).collect(Collectors.joining(","))))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class);
  }
}
