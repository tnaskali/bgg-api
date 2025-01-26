package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggSearchV5QueryParams;
import li.naska.bgg.repository.model.BggSearchV5ResponseBody;
import li.naska.bgg.resource.v5.model.SearchContext;
import li.naska.bgg.util.JsonProcessor;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class BggSearchV5Repository {

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  public BggSearchV5Repository(
      @Value("${bgg.endpoints.v5.search}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<BggSearchV5ResponseBody> getSearchResults(
      SearchContext context, BggSearchV5QueryParams params) {
    return getSearchResultsAsJson(context, params)
        .map(body -> jsonProcessor.toJavaObject(body, BggSearchV5ResponseBody.class));
  }

  public Mono<String> getSearchResultsAsJson(SearchContext context, BggSearchV5QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/{context}")
            .queryParams(QueryParameters.fromPojo(params))
            .build(context))
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
}
