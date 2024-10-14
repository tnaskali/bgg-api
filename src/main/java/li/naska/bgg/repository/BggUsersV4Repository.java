package li.naska.bgg.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import java.nio.charset.StandardCharsets;
import java.util.List;
import li.naska.bgg.exception.UnexpectedServerResponseException;
import li.naska.bgg.repository.model.BggUsersV4QueryParams;
import li.naska.bgg.repository.model.BggUsersV4ResponseBody;
import li.naska.bgg.util.JsonProcessor;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class BggUsersV4Repository {

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  public BggUsersV4Repository(
      @Value("${bgg.endpoints.v4.users}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<List<BggUsersV4ResponseBody>> getUsers(BggUsersV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK) {
            return UnexpectedServerResponseException.from(clientResponse).buildAndThrow();
          }
          return clientResponse
              .bodyToMono(String.class)
              .defaultIfEmpty("")
              .map(body -> jsonProcessor.toJavaObject(body, new TypeReference<>() {}));
        });
  }

  public Mono<BggUsersV4ResponseBody> getUser(Integer id) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/{id}").build(id))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK) {
            return UnexpectedServerResponseException.from(clientResponse).buildAndThrow();
          }
          return clientResponse
              .bodyToMono(String.class)
              .defaultIfEmpty("")
              .map(body -> jsonProcessor.toJavaObject(body, BggUsersV4ResponseBody.class));
        });
  }
}
