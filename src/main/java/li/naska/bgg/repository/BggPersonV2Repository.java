package li.naska.bgg.repository;

import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.repository.model.BggPersonV2QueryParams;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Repository
public class BggPersonV2Repository {

  private final WebClient webClient;

  public BggPersonV2Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v2.person}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<String> getPersons(BggPersonV2QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .build())
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
