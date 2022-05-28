package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import li.naska.bgg.exception.BggConnectionException;
import li.naska.bgg.repository.model.BggUnifiedSearchQueryParams;
import li.naska.bgg.repository.model.BggUnifiedSearchResponseBody;
import li.naska.bgg.resource.v2.model.UnifiedSearchDomain;
import li.naska.bgg.util.QueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
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

@Repository
public class BggUnifiedSearchRepository {

  private final WebClient unifiedSearchWebClient;

  public BggUnifiedSearchRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.unifiedsearch}") String geekplayEndpoint) {
    this.unifiedSearchWebClient = builder.baseUrl(geekplayEndpoint + "/{domain}").build();
  }

  public Mono<BggUnifiedSearchResponseBody> getSearchResults(UnifiedSearchDomain domain, BggUnifiedSearchQueryParams params) {
    return unifiedSearchWebClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(QueryParameters.fromPojo(params))
            .build(domain))
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .toEntity(String.class)
        .onErrorMap(IOException.class, ioe -> new BggConnectionException())
        .retryWhen(
            Retry.max(3)
                .filter(throwable -> throwable instanceof BggConnectionException))
        .map(entity -> {
          try {
            return new ObjectMapper().readValue(entity.getBody(), BggUnifiedSearchResponseBody.class);
          } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
          }
        });
  }

}
