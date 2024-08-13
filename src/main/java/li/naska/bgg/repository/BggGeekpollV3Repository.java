package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import li.naska.bgg.repository.model.BggGeekpollV3QueryParams;
import li.naska.bgg.repository.model.BggGeekpollV3ResponseBody;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Repository
public class BggGeekpollV3Repository {

  @Autowired
  private ObjectMapper objectMapper;

  private final WebClient webClient;

  public BggGeekpollV3Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v3.geekpoll}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<BggGeekpollV3ResponseBody> getGeekpoll(Optional<String> cookie, BggGeekpollV3QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .headers(headers -> cookie.ifPresent(c -> headers.add(HttpHeaders.COOKIE, c)))
        .retrieve()
        .toEntity(String.class)
        .doOnNext(entity -> {
              if (JsonPath.read(entity.getBody(), "$['poll']").equals(false)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll does not exist");
              }
            }
        )
        .handle((entity, sink) -> {
          try {
            sink.next(objectMapper.readValue(entity.getBody(), BggGeekpollV3ResponseBody.class));
          } catch (JsonProcessingException e) {
            sink.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
          }
        });
  }

}
