package li.naska.bgg.repository;

import li.naska.bgg.repository.model.BggFamilyParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Repository
public class BggFamilyRepository {

  private final WebClient webClient;

  public BggFamilyRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.family}") String familyEndpoint) {
    this.webClient = builder.baseUrl(familyEndpoint).build();
  }

  public Mono<String> getFamily(BggFamilyParameters parameters) {
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
