package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggItemWeblinksV5QueryParams;
import li.naska.bgg.repository.model.BggItemWeblinksV5ResponseBody;
import li.naska.bgg.util.JsonProcessor;
import li.naska.bgg.util.QueryParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class BggItemV5Repository {

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  public BggItemV5Repository(
      @Value("${bgg.endpoints.v5.item}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<BggItemWeblinksV5ResponseBody> getItemWeblinks(BggItemWeblinksV5QueryParams params) {
    return getItemWeblinksAsJson(params)
        .map(body -> jsonProcessor.toJavaObject(body, BggItemWeblinksV5ResponseBody.class));
  }

  public Mono<String> getItemWeblinksAsJson(BggItemWeblinksV5QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/weblinks")
            .queryParams(QueryParameters.fromPojo(params))
            .build())
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Forum not found");
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
}
