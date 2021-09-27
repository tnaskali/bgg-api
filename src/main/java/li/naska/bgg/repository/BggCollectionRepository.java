package li.naska.bgg.repository;

import com.boardgamegeek.collection.Collection;
import com.boardgamegeek.enums.ObjectSubtype;
import li.naska.bgg.exception.BggResponseNotReadyException;
import li.naska.bgg.repository.model.BggUserItemsParameters;
import li.naska.bgg.util.QueryParamFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

@Repository
public class BggCollectionRepository {

  private final WebClient webClient;

  public BggCollectionRepository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.collection.read}") String collectionReadEndpoint) {
    this.webClient = builder.baseUrl(collectionReadEndpoint).build();
  }

  private static MultiValueMap<String, String> extractUserItemsParams(BggUserItemsParameters params) {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("username", params.getUsername());
    Optional.ofNullable(params.getVersion()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("version", e));
    Optional.ofNullable(params.getBrief()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("brief", e));
    Optional.ofNullable(params.getStats()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("stats", e));
    // BGG API: format="comma-separated"
    Optional.ofNullable(params.getIds()).map(QueryParamFunctions.BGG_INTEGER_LIST_FUNCTION).ifPresent(e -> map.set("id", e));
    Optional.ofNullable(params.getSubtype()).map(QueryParamFunctions.BGG_OBJECT_SUBTYPE_FUNCTION).ifPresent(e -> map.set("subtype", e));
    Optional.ofNullable(params.getExcludesubtype()).map(QueryParamFunctions.BGG_OBJECT_SUBTYPE_FUNCTION).ifPresent(e -> map.set("excludesubtype", e));
    Optional.ofNullable(params.getOwn()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("own", e));
    Optional.ofNullable(params.getRated()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("rated", e));
    Optional.ofNullable(params.getPlayed()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("played", e));
    Optional.ofNullable(params.getComment()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("comment", e));
    Optional.ofNullable(params.getTrade()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("trade", e));
    Optional.ofNullable(params.getWant()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("want", e));
    Optional.ofNullable(params.getWishlist()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("wishlist", e));
    Optional.ofNullable(params.getWishlistpriority()).map(Object::toString).ifPresent(e -> map.set("wishlistpriority", e));
    Optional.ofNullable(params.getPreordered()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("preordered", e));
    Optional.ofNullable(params.getWanttoplay()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("wanttoplay", e));
    Optional.ofNullable(params.getWanttobuy()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("wanttobuy", e));
    Optional.ofNullable(params.getPrevowned()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("prevowned", e));
    Optional.ofNullable(params.getHasparts()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("hasparts", e));
    Optional.ofNullable(params.getWantparts()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("wantparts", e));
    Optional.ofNullable(params.getMinrating()).map(QueryParamFunctions.BGG_BIGDECIMAL_FUNCTION).ifPresent(e -> map.set("minrating", e));
    // BGG API: name="rating"
    Optional.ofNullable(params.getMaxrating()).map(QueryParamFunctions.BGG_BIGDECIMAL_FUNCTION).ifPresent(e -> map.set("rating", e));
    Optional.ofNullable(params.getMinbggrating()).map(QueryParamFunctions.BGG_BIGDECIMAL_FUNCTION).ifPresent(e -> map.set("minbggrating", e));
    // BGG API: name="bggrating"
    Optional.ofNullable(params.getMaxbggrating()).map(QueryParamFunctions.BGG_BIGDECIMAL_FUNCTION).ifPresent(e -> map.set("bggrating", e));
    Optional.ofNullable(params.getMinplays()).map(Object::toString).ifPresent(e -> map.set("minplays", e));
    Optional.ofNullable(params.getMaxplays()).map(Object::toString).ifPresent(e -> map.set("maxplays", e));
    Optional.ofNullable(params.getShowprivate()).map(QueryParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("showprivate", e));
    Optional.ofNullable(params.getCollid()).map(Object::toString).ifPresent(e -> map.set("collid", e));
    if (params.getModifiedsincedate() != null) {
      if (params.getModifiedsincetime() != null) {
        map.set("minarticledate", QueryParamFunctions.BGG_LOCALDATETIME_FUNCTION.apply(params.getModifiedsincedate().atTime(params.getModifiedsincetime())));
      } else {
        map.set("minarticledate", QueryParamFunctions.BGG_LOCALDATE_FUNCTION.apply(params.getModifiedsincedate()));
      }
    }
    return map;
  }

  public Mono<Collection> getItems(BggUserItemsParameters params) {
    // handle subtype bug in the BBG XML API
    if (params.getSubtype() == null || params.getSubtype().equals(ObjectSubtype.BOARDGAME)) {
      params.setExcludesubtype(ObjectSubtype.BOARDGAMEEXPANSION);
    }

    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .queryParams(extractUserItemsParams(params))
            .build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(
            // BGG might queue the request
            status -> status == HttpStatus.ACCEPTED,
            response -> Mono.error(new BggResponseNotReadyException()))
        .bodyToMono(Collection.class)
        .retryWhen(
            Retry.backoff(6, Duration.ofSeconds(5))
                .filter(throwable -> throwable instanceof BggResponseNotReadyException));
  }

}
