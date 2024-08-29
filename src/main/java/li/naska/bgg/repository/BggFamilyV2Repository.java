package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.repository.model.BggFamilyV2QueryParams;
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
public class BggFamilyV2Repository {

  private final WebClient webClient;

  public BggFamilyV2Repository(
      @Autowired WebClient.Builder builder, @Value("${bgg.endpoints.v2.family}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<String> getFamilies(BggFamilyV2QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(
            httpStatus -> httpStatus == HttpStatus.BAD_REQUEST,
            clientResponse -> Mono.error(
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown remote error")))
        .bodyToMono(String.class);
  }
}
