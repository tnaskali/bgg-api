package li.naska.bgg.resource.v1;

import com.boardgamegeek.collection.Collection;
import com.boardgamegeek.enums.DomainType;
import com.boardgamegeek.enums.ObjectSubtype;
import com.boardgamegeek.enums.ObjectType;
import com.boardgamegeek.plays.Plays;
import com.boardgamegeek.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggCollectionRepository;
import li.naska.bgg.repository.BggPlaysRepository;
import li.naska.bgg.repository.BggUsersRepository;
import li.naska.bgg.repository.model.BggUserItemsParameters;
import li.naska.bgg.repository.model.BggUserParameters;
import li.naska.bgg.repository.model.BggUserPlaysParameters;
import li.naska.bgg.repository.model.plays.BggPlay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UsersResource {

  @Autowired
  private BggCollectionRepository bggCollectionService;

  @Autowired
  private BggPlaysRepository bggPlaysService;

  @Autowired
  private BggUsersRepository bggUsersService;

  @GetMapping(value = "/{username}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public Mono<User> getUser(
      @PathVariable(value = "username") String username,
      @RequestParam(value = "buddies", required = false) Boolean buddies,
      @RequestParam(value = "guilds", required = false) Boolean guilds,
      @RequestParam(value = "hot", required = false) Boolean hot,
      @RequestParam(value = "top", required = false) Boolean top,
      @RequestParam(value = "domain", required = false) DomainType domain,
      @RequestParam(value = "page", required = false) Integer page
  ) {
    BggUserParameters parameters = new BggUserParameters(username);
    parameters.setBuddies(buddies);
    parameters.setGuilds(guilds);
    parameters.setHot(hot);
    parameters.setTop(top);
    parameters.setDomain(domain);
    parameters.setPage(page);
    return bggUsersService.getUser(parameters);
  }

  @GetMapping(value = "/{username}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Plays> getPlays(
      @PathVariable(value = "username") String username,
      @RequestParam(value = "id", required = false) Integer id,
      @RequestParam(value = "type", required = false) ObjectType type,
      @RequestParam(value = "mindate", required = false) LocalDate mindate,
      @RequestParam(value = "maxdate", required = false) LocalDate maxdate,
      @RequestParam(value = "subtype", required = false) ObjectSubtype subtype,
      @RequestParam(value = "page", required = false) Integer page
  ) {
    BggUserPlaysParameters parameters = new BggUserPlaysParameters(username);
    parameters.setId(id);
    parameters.setType(type);
    parameters.setMindate(mindate);
    parameters.setMaxdate(maxdate);
    parameters.setSubtype(subtype);
    parameters.setPage(page);
    return bggPlaysService.getPlays(parameters);
  }

  @PostMapping(value = "/{username}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<Map<String, Object>> createPlay(
      @PathVariable(value = "username") String username,
      @RequestBody BggPlay play
  ) {
    return bggPlaysService.savePlay(username, null, play);
  }

  @PutMapping(value = "/{username}/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<Map<String, Object>> updatePlay(
      @PathVariable(value = "username") String username,
      @PathVariable(value = "id") Integer id,
      @RequestBody BggPlay play
  ) {
    return bggPlaysService.savePlay(username, id, play);
  }

  @DeleteMapping(value = "/{username}/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<Map<String, Object>> deletePlay(
      @PathVariable(value = "username") String username,
      @PathVariable(value = "id") Integer id
  ) {
    return bggPlaysService.deletePlay(username, id);
  }

  @GetMapping(value = "/{username}/items", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Collection> getItems(
      @PathVariable(value = "username") String username,
      @RequestParam(value = "version", required = false) Boolean version,
      @RequestParam(value = "subtype", required = false) ObjectSubtype subtype,
      @RequestParam(value = "excludesubtype", required = false) ObjectSubtype excludesubtype,
      @RequestParam(value = "id", required = false) List<Integer> ids,
      @RequestParam(value = "brief", required = false) Boolean brief,
      @RequestParam(value = "stats", required = false) Boolean stats,
      @RequestParam(value = "own", required = false) Boolean own,
      @RequestParam(value = "rated", required = false) Boolean rated,
      @RequestParam(value = "played", required = false) Boolean played,
      @RequestParam(value = "comment", required = false) Boolean comment,
      @RequestParam(value = "trade", required = false) Boolean trade,
      @RequestParam(value = "want", required = false) Boolean want,
      @RequestParam(value = "wishlist", required = false) Boolean wishlist,
      @RequestParam(value = "wishlistpriority", required = false) Integer wishlistpriority,
      @RequestParam(value = "preordered", required = false) Boolean preordered,
      @RequestParam(value = "wanttoplay", required = false) Boolean wanttoplay,
      @RequestParam(value = "wanttobuy", required = false) Boolean wanttobuy,
      @RequestParam(value = "prevowned", required = false) Boolean prevowned,
      @RequestParam(value = "hasparts", required = false) Boolean hasparts,
      @RequestParam(value = "wantparts", required = false) Boolean wantparts,
      @RequestParam(value = "minrating", required = false) BigDecimal minrating,
      @RequestParam(value = "rating", required = false) BigDecimal maxrating,
      @RequestParam(value = "minbggrating", required = false) BigDecimal minbggrating,
      @RequestParam(value = "bggrating", required = false) BigDecimal maxbggrating,
      @RequestParam(value = "minplays", required = false) Integer minplays,
      @RequestParam(value = "maxplays", required = false) Integer maxplays,
      @RequestParam(value = "showprivate", required = false) Boolean showprivate,
      @RequestParam(value = "collid", required = false) Integer collid,
      @RequestParam(value = "modifiedsincedate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate modifiedsincedate,
      @RequestParam(value = "modifiedsincetime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime modifiedsincetime
  ) {
    BggUserItemsParameters parameters = new BggUserItemsParameters(username);
    parameters.setVersion(version);
    parameters.setBrief(brief);
    parameters.setStats(stats);
    parameters.setIds(ids);
    parameters.setSubtype(subtype);
    parameters.setExcludesubtype(excludesubtype);
    parameters.setOwn(own);
    parameters.setRated(rated);
    parameters.setPlayed(played);
    parameters.setComment(comment);
    parameters.setTrade(trade);
    parameters.setWant(want);
    parameters.setWishlist(wishlist);
    parameters.setWishlistpriority(wishlistpriority);
    parameters.setPreordered(preordered);
    parameters.setWanttoplay(wanttoplay);
    parameters.setWanttobuy(wanttobuy);
    parameters.setPrevowned(prevowned);
    parameters.setHasparts(hasparts);
    parameters.setWantparts(wantparts);
    parameters.setMinrating(minrating);
    // BGG API: name="rating"
    parameters.setMaxrating(maxrating);
    parameters.setMinbggrating(minbggrating);
    // BGG API: name="bggrating"
    parameters.setMaxbggrating(maxbggrating);
    parameters.setMinplays(minplays);
    parameters.setMaxplays(maxplays);
    parameters.setShowprivate(showprivate);
    parameters.setCollid(collid);
    parameters.setModifiedsincedate(modifiedsincedate);
    parameters.setModifiedsincetime(modifiedsincetime);
    return bggCollectionService.getItems(parameters);
  }

}
