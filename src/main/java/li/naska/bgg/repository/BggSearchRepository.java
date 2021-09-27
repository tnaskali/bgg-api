package li.naska.bgg.repository;

import com.boardgamegeek.search.Results;
import li.naska.bgg.repository.model.BggSearchParameters;
import li.naska.bgg.util.QueryParamFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Repository
public class BggSearchRepository {

  private final WebClient webClient;

  public BggSearchRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.search.read}") String searchReadEndpoint) {
    this.webClient = builder.baseUrl(searchReadEndpoint).build();
  }

  private static MultiValueMap<String, String> extractFamiliesParams(BggSearchParameters params) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("query", params.getQuery());
    Optional.ofNullable(params.getTypes()).map(QueryParamFunctions.BGG_OBJECT_SUBTYPE_LIST_FUNCTION).ifPresent(e -> map.set("type", e));
    Optional.ofNullable(params.getExact()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("exact", e));
    return map;
  }

  public Mono<Results> getItems(BggSearchParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(extractFamiliesParams(parameters))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Results.class);
  }

}
