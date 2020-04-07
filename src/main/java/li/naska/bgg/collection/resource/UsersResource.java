package li.naska.bgg.collection.resource;

import com.boardgamegeek.xmlapi2.collection.ItemSubtypeEnum;
import com.boardgamegeek.xmlapi2.collection.Items;
import com.boardgamegeek.xmlapi2.plays.ItemTypeEnum;
import com.boardgamegeek.xmlapi2.plays.Plays;
import com.boardgamegeek.xmlapi2.user.ListDomainEnum;
import com.boardgamegeek.xmlapi2.user.User;
import li.naska.bgg.collection.service.CollectionService;
import li.naska.bgg.collection.service.PlaysService;
import li.naska.bgg.collection.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/users")
public class UsersResource {

  private static final DateTimeFormatter LOCALDATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Autowired
  private CollectionService collectionService;

  @Autowired
  private PlaysService playsService;

  @Autowired
  private UsersService usersService;

  @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<User> getUser(
      @PathVariable(value = "username") String username,
      @RequestParam(value = "buddies", required = false) Optional<Boolean> buddies,
      @RequestParam(value = "guilds", required = false) Optional<Boolean> guilds,
      @RequestParam(value = "hot", required = false) Optional<Boolean> hot,
      @RequestParam(value = "top", required = false) Optional<Boolean> top,
      @RequestParam(value = "domain", required = false) Optional<ListDomainEnum> domain,
      @RequestParam(value = "page", required = false) Optional<Integer> page
  ) {
    Stream<Entry<String, Optional<String>>> stream = Stream.of(
        new SimpleEntry<>("buddies", buddies.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("guilds", guilds.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("hot", hot.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("top", top.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("domain", domain.map(ListDomainEnum::value)),
        new SimpleEntry<>("page", page.map(Object::toString))
    );
    Map<String, String> params = stream
        .filter(e -> e.getValue().isPresent())
        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().get()));
    ResponseEntity<User> response = usersService.getUser(username, params);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

  @GetMapping(value = "/{username}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Plays> getPlays(
      @PathVariable(value = "username") String username,
      @RequestParam(value = "id", required = false) Optional<Integer> id,
      @RequestParam(value = "type", required = false) Optional<ItemTypeEnum> type,
      @RequestParam(value = "mindate", required = false) Optional<LocalDate> mindate,
      @RequestParam(value = "maxdate", required = false) Optional<LocalDate> maxdate,
      @RequestParam(value = "subtype", required = false) Optional<com.boardgamegeek.xmlapi2.plays.ItemSubtypeEnum> subtype,
      @RequestParam(value = "page", required = false) Optional<Integer> page
  ) {
    Stream<Entry<String, Optional<String>>> stream = Stream.of(
        // BGG API: format="comma-separated"
        new SimpleEntry<>("id", id.map(Object::toString)),
        new SimpleEntry<>("type", type.map(ItemTypeEnum::value)),
        new SimpleEntry<>("mindate", mindate.map(LOCALDATETIME_FORMATTER::format)),
        new SimpleEntry<>("maxdate", maxdate.map(LOCALDATETIME_FORMATTER::format)),
        new SimpleEntry<>("subtype", subtype.map(com.boardgamegeek.xmlapi2.plays.ItemSubtypeEnum::value)),
        new SimpleEntry<>("page", page.map(Object::toString))
    );
    Map<String, String> params = stream
        .filter(e -> e.getValue().isPresent())
        .collect(Collectors.toMap(Entry::getKey, e -> e.getValue().get()));
    ResponseEntity<Plays> response = playsService.getPlays(username, params);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

  @GetMapping(value = "/{username}/items", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Items> getItems(
      @PathVariable(value = "username") String username,
      @RequestParam(value = "version", required = false) Optional<Boolean> version,
      @RequestParam(value = "subtype", required = false) Optional<ItemSubtypeEnum> subtype,
      @RequestParam(value = "excludesubtype", required = false) Optional<ItemSubtypeEnum> excludesubtype,
      @RequestParam(value = "id", required = false) Optional<List<Integer>> id,
      @RequestParam(value = "brief", required = false) Optional<Boolean> brief,
      @RequestParam(value = "stats", required = false) Optional<Boolean> stats,
      @RequestParam(value = "own", required = false) Optional<Boolean> own,
      @RequestParam(value = "rated", required = false) Optional<Boolean> rated,
      @RequestParam(value = "played", required = false) Optional<Boolean> played,
      @RequestParam(value = "comment", required = false) Optional<Boolean> comment,
      @RequestParam(value = "trade", required = false) Optional<Boolean> trade,
      @RequestParam(value = "want", required = false) Optional<Boolean> want,
      @RequestParam(value = "wishlist", required = false) Optional<Boolean> wishlist,
      @RequestParam(value = "wishlistpriority", required = false) Optional<Integer> wishlistpriority,
      @RequestParam(value = "preordered", required = false) Optional<Boolean> preordered,
      @RequestParam(value = "wanttoplay", required = false) Optional<Boolean> wanttoplay,
      @RequestParam(value = "wanttobuy", required = false) Optional<Boolean> wanttobuy,
      @RequestParam(value = "prevowned", required = false) Optional<Boolean> prevowned,
      @RequestParam(value = "hasparts", required = false) Optional<Boolean> hasparts,
      @RequestParam(value = "wantparts", required = false) Optional<Boolean> wantparts,
      @RequestParam(value = "minrating", required = false) Optional<BigDecimal> minrating,
      @RequestParam(value = "rating", required = false) Optional<BigDecimal> maxrating,
      @RequestParam(value = "minbggrating", required = false) Optional<BigDecimal> minbggrating,
      @RequestParam(value = "bggrating", required = false) Optional<BigDecimal> maxbggrating,
      @RequestParam(value = "minplays", required = false) Optional<Integer> minplays,
      @RequestParam(value = "maxplays", required = false) Optional<Integer> maxplays,
      @RequestParam(value = "showprivate", required = false) Optional<Boolean> showprivate,
      @RequestParam(value = "collid", required = false) Optional<Integer> collid,
      @RequestParam(value = "modifiedsince", required = false) Optional<LocalDate> modifiedsince
      ) {
    Stream<Entry<String, Optional<String>>> stream = Stream.of(
        new SimpleEntry<>("version", version.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("brief", brief.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("stats", stats.map(e -> e ? "1" : "0")),
        // BGG API: format="comma-separated"
        new SimpleEntry<>("id", id.map(e -> e.stream().map(Object::toString).collect(Collectors.joining(",")))),
        new SimpleEntry<>("subtype", subtype.map(ItemSubtypeEnum::value)),
        new SimpleEntry<>("excludesubtype", excludesubtype.map(ItemSubtypeEnum::value)),
        new SimpleEntry<>("own", own.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("rated", rated.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("played", played.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("comment", comment.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("trade", trade.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("want", want.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("wishlist", wishlist.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("wishlistpriority", wishlistpriority.map(Object::toString)),
        new SimpleEntry<>("preordered", preordered.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("wanttoplay", wanttoplay.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("wanttobuy", wanttobuy.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("prevowned", prevowned.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("hasparts", hasparts.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("wantparts", wantparts.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("minrating", minrating.map(e -> e.setScale(5, RoundingMode.FLOOR).toPlainString())),
        // BGG API: name="rating"
        new SimpleEntry<>("rating", maxrating.map(e -> e.setScale(5, RoundingMode.FLOOR).toPlainString())),
        new SimpleEntry<>("minbggrating", minbggrating.map(e -> e.setScale(5, RoundingMode.FLOOR).toPlainString())),
        // BGG API: name="bggrating"
        new SimpleEntry<>("bggrating", maxbggrating.map(e -> e.setScale(5, RoundingMode.FLOOR).toPlainString())),
        new SimpleEntry<>("minplays", minplays.map(Object::toString)),
        new SimpleEntry<>("maxplays", maxplays.map(Object::toString)),
        new SimpleEntry<>("showprivate", showprivate.map(e -> e ? "1" : "0")),
        new SimpleEntry<>("collid", collid.map(Object::toString)),
        new SimpleEntry<>("modifiedsince", modifiedsince.map(LOCALDATETIME_FORMATTER::format))
    );
    Map<String, String> params = stream
        .filter(e -> e.getValue().isPresent())
        .collect(Collectors.toMap(Entry::getKey, e -> e.getValue().get()));
    ResponseEntity<Items> response = collectionService.getItems(username, params);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

}
