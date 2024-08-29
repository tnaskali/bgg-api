package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.repository.model.BggThreadV1QueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class BggThreadV1Repository {

  private final WebClient webClient;

  public BggThreadV1Repository(
      @Autowired WebClient.Builder builder, @Value("${bgg.endpoints.v1.thread}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<String> getThread(Integer id, BggThreadV1QueryParams params) {
    return webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder.path("/{ids}").queryParams(QueryParameters.fromPojo(params)).build(id))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(String.class);
  }
}
