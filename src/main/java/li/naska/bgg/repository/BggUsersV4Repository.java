package li.naska.bgg.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggCurrentUserV4ResponseBody;
import li.naska.bgg.repository.model.BggUserV4ResponseBody;
import li.naska.bgg.repository.model.BggUsersV4QueryParams;
import li.naska.bgg.util.JsonProcessor;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
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

  public Mono<List<BggUserV4ResponseBody>> getUsers(BggUsersV4QueryParams params) {
    return getUsersAsJson(params)
        .map(body -> jsonProcessor.toJavaObject(body, new TypeReference<>() {}));
  }

  public Mono<BggUserV4ResponseBody> getUser(BggUsersV4QueryParams params) {
    return getUsers(params).flatMap(users -> {
      if (users.isEmpty()) {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
      } else if (users.size() > 1) {
        return Mono.error(
            new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Multiple users found"));
      }
      return Mono.just(users.get(0));
    });
  }

  public Mono<String> getUsersAsJson(BggUsersV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK
              || clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.APPLICATION_JSON::equalsTypeAndSubtype)
                  .isEmpty()) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        });
  }

  public Mono<BggUserV4ResponseBody> getUser(Integer id) {
    return getUserAsJson(id)
        .map(body -> jsonProcessor.toJavaObject(body, BggUserV4ResponseBody.class));
  }

  public Mono<String> getUserAsJson(Integer id) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/{id}").build(id))
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
          } else if (clientResponse.statusCode() != HttpStatus.OK
              || clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.APPLICATION_JSON::equalsTypeAndSubtype)
                  .isEmpty()) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        });
  }

  public Mono<BggCurrentUserV4ResponseBody> getCurrentUser(Optional<String> cookie) {
    return getCurrentUserAsJson(cookie)
        .map(body -> jsonProcessor.toJavaObject(body, BggCurrentUserV4ResponseBody.class));
  }

  public Mono<String> getCurrentUserAsJson(Optional<String> cookie) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/current").build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .headers(headers -> cookie.ifPresent(c -> headers.add(HttpHeaders.COOKIE, c)))
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK
              || clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.APPLICATION_JSON::equalsTypeAndSubtype)
                  .isEmpty()) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        });
  }
}
