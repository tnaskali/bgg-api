package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.repository.model.BggGeekpollV3RequestParams;
import li.naska.bgg.repository.model.BggGeekpollV3ResponseBody;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Repository
public class BggGeekpollV3Repository {

  private final WebClient webClient;
  @Autowired
  @Qualifier("v3")
  private ObjectMapper objectMapper;

  public BggGeekpollV3Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v3.geekpoll}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<BggGeekpollV3ResponseBody> getGeekpoll(Optional<String> cookie, BggGeekpollV3RequestParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .headers(headers -> cookie.ifPresent(c -> headers.add("Cookie", c)))
        .retrieve()
        .toEntity(String.class)
        .onErrorMap(IOException.class, ioe -> new BggConnectionException())
        .retryWhen(
            Retry.max(3)
                .filter(throwable -> throwable instanceof BggConnectionException))
        .doOnNext(entity -> {
              if (JsonPath.read(entity.getBody(), "$['poll']").equals(false)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll does not exist");
              }
            }
        )
        .map(entity -> {
          try {
            return objectMapper.readValue(entity.getBody(), BggGeekpollV3ResponseBody.class);
          } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
          }
        });
  }

}