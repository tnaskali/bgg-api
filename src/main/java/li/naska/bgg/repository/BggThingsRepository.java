package li.naska.bgg.repository;

import com.boardgamegeek.thing.Things;
import li.naska.bgg.repository.model.BggThingsParameters;
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
public class BggThingsRepository {

  private final WebClient webClient;

  public BggThingsRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.thing.read}") String thingReadEndpoint) {
    this.webClient = builder.baseUrl(thingReadEndpoint).build();
  }

  private static MultiValueMap<String, String> extractFamiliesParams(BggThingsParameters params) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("id", QueryParamFunctions.BGG_INTEGER_LIST_FUNCTION.apply(params.getIds()));
    Optional.ofNullable(params.getType()).map(QueryParamFunctions.BGG_OBJECT_SUBTYPE_LIST_FUNCTION).ifPresent(e -> map.set("type", e));
    Optional.ofNullable(params.getVersions()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("versions", e));
    Optional.ofNullable(params.getVideos()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("videos", e));
    Optional.ofNullable(params.getStats()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("stats", e));
    // BGG API: not currently supported
    // Optional.ofNullable(params.getHistorical()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("historical", e));
    Optional.ofNullable(params.getMarketplace()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("marketplace", e));
    Optional.ofNullable(params.getComments()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("comments", e));
    Optional.ofNullable(params.getRatingcomments()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("ratingcomments", e));
    Optional.ofNullable(params.getPage()).map(Object::toString).ifPresent(e -> map.set("page", e));
    Optional.ofNullable(params.getPagesize()).map(Object::toString).ifPresent(e -> map.set("pagesize", e));
    // BGG API: not currently supported
    // Optional.ofNullable(params.getFrom()).map(QueryParamFunctions.BGG_LOCALDATE_FUNCTION).ifPresent(e -> map.set("from", e));
    // Optional.ofNullable(params.getTo()).map(QueryParamFunctions.BGG_LOCALDATE_FUNCTION).ifPresent(e -> map.set("to", e));
    return map;
  }

  public Mono<Things> getThings(BggThingsParameters parameters) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(extractFamiliesParams(parameters))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .bodyToMono(Things.class);
  }

}
