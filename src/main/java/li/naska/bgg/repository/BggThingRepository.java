package li.naska.bgg.repository;

import li.naska.bgg.repository.model.BggThingsParameters;
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
public class BggThingRepository {

  private final WebClient webClient;

  public BggThingRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.thing}") String thingEndpoint) {
    this.webClient = builder.baseUrl(thingEndpoint).build();
  }

  public Mono<String> getThings(BggThingsParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(parameters.toMultiValueMap())
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(
            httpStatus -> httpStatus == HttpStatus.BAD_REQUEST,
            clientResponse -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown remote error")))
        .bodyToMono(String.class);
  }

}
