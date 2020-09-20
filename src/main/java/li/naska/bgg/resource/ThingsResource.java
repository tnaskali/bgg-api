package li.naska.bgg.resource;


import com.boardgamegeek.enums.HotItemType;
import com.boardgamegeek.enums.ObjectSubtype;
import com.boardgamegeek.enums.ObjectType;
import com.boardgamegeek.hot.HotItems;
import com.boardgamegeek.plays.Plays;
import com.boardgamegeek.search.Results;
import com.boardgamegeek.thing.Things;
import li.naska.bgg.service.BggHotItemsService;
import li.naska.bgg.service.BggPlaysService;
import li.naska.bgg.service.BggSearchService;
import li.naska.bgg.service.BggThingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/things")
public class ThingsResource {

  private static final DateTimeFormatter LOCALDATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Autowired
  private BggHotItemsService bggHotItemsService;

  @Autowired
  private BggPlaysService bggPlaysService;

  @Autowired
  private BggSearchService bggSearchService;

  @Autowired
  private BggThingsService bggThingsService;

  @GetMapping(value = "/hot", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HotItems> getThings() {
    ResponseEntity<HotItems> response = bggHotItemsService.getItems(HotItemType.BOARDGAME);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Results> searchThings(
      @RequestParam(value = "query") String query,
      @RequestParam(value = "type", required = false) Optional<List<ObjectSubtype>> types,
      @RequestParam(value = "exact", required = false) Optional<Boolean> exact
  ) {
    Stream<Map.Entry<String, Optional<String>>> stream = Stream.of(
        new AbstractMap.SimpleEntry<>("type", types.map(e -> e.stream().map(ObjectSubtype::value).collect(Collectors.joining(",")))),
        new AbstractMap.SimpleEntry<>("exact", exact.map(e -> e ? "1" : "0"))
    );
    Map<String, String> params = stream
        .filter(e -> e.getValue().isPresent())
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    ResponseEntity<Results> response = bggSearchService.getItems(query, params);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Things> getThings(
      @RequestParam(value = "id") List<Integer> ids,
      @RequestParam(value = "type", required = false) Optional<List<ObjectSubtype>> types,
      @RequestParam(value = "versions", required = false) Optional<Boolean> versions,
      @RequestParam(value = "videos", required = false) Optional<Boolean> videos,
      @RequestParam(value = "stats", required = false) Optional<Boolean> stats,
      @RequestParam(value = "historical", required = false) Optional<Boolean> historical,
      @RequestParam(value = "marketplace", required = false) Optional<Boolean> marketplace,
      @RequestParam(value = "comments", required = false) Optional<Boolean> comments,
      @RequestParam(value = "ratingcomments", required = false) Optional<Boolean> ratingcomments,
      @RequestParam(value = "page", required = false) Optional<Integer> page,
      @RequestParam(value = "pagesize", required = false) Optional<Integer> pagesize
  ) {
    Stream<Map.Entry<String, Optional<String>>> stream = Stream.of(
        new AbstractMap.SimpleEntry<>("type", types.map(e -> e.stream().map(ObjectSubtype::value).collect(Collectors.joining(",")))),
        new AbstractMap.SimpleEntry<>("versions", versions.map(e -> e ? "1" : "0")),
        new AbstractMap.SimpleEntry<>("videos", videos.map(e -> e ? "1" : "0")),
        new AbstractMap.SimpleEntry<>("stats", stats.map(e -> e ? "1" : "0")),
        new AbstractMap.SimpleEntry<>("historical", historical.map(e -> e ? "1" : "0")),
        new AbstractMap.SimpleEntry<>("marketplace", marketplace.map(e -> e ? "1" : "0")),
        new AbstractMap.SimpleEntry<>("versions", versions.map(e -> e ? "1" : "0")),
        new AbstractMap.SimpleEntry<>("comments", comments.map(e -> e ? "1" : "0")),
        new AbstractMap.SimpleEntry<>("ratingcomments", ratingcomments.map(e -> e ? "1" : "0")),
        new AbstractMap.SimpleEntry<>("page", page.map(Object::toString)),
        new AbstractMap.SimpleEntry<>("pagesize", pagesize.map(Object::toString))
    );
    Map<String, String> params = stream
        .filter(e -> e.getValue().isPresent())
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    ResponseEntity<Things> response = bggThingsService.getThings(ids.stream().map(Object::toString).collect(Collectors.joining(",")), params);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Things> getThing(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "versions", required = false) Optional<Boolean> versions,
      @RequestParam(value = "videos", required = false) Optional<Boolean> videos,
      @RequestParam(value = "stats", required = false) Optional<Boolean> stats,
      @RequestParam(value = "historical", required = false) Optional<Boolean> historical,
      @RequestParam(value = "marketplace", required = false) Optional<Boolean> marketplace,
      @RequestParam(value = "comments", required = false) Optional<Boolean> comments,
      @RequestParam(value = "ratingcomments", required = false) Optional<Boolean> ratingcomments
  ) {
    Stream<Map.Entry<String, Optional<String>>> stream = Stream.of(
        new AbstractMap.SimpleEntry<>("versions", versions.map(e -> e ? "1" : "0")),
        new AbstractMap.SimpleEntry<>("videos", videos.map(e -> e ? "1" : "0")),
        new AbstractMap.SimpleEntry<>("stats", stats.map(e -> e ? "1" : "0")),
        new AbstractMap.SimpleEntry<>("historical", historical.map(e -> e ? "1" : "0")),
        new AbstractMap.SimpleEntry<>("marketplace", marketplace.map(e -> e ? "1" : "0")),
        new AbstractMap.SimpleEntry<>("comments", comments.map(e -> e ? "1" : "0")),
        new AbstractMap.SimpleEntry<>("ratingcomments", ratingcomments.map(e -> e ? "1" : "0"))
    );
    Map<String, String> params = stream
        .filter(e -> e.getValue().isPresent())
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    ResponseEntity<Things> response = bggThingsService.getThings(id.toString(), params);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

  @GetMapping(value = "/{id}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Plays> getPlays(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "username", required = false) Optional<String> username,
      @RequestParam(value = "mindate", required = false) Optional<LocalDate> mindate,
      @RequestParam(value = "maxdate", required = false) Optional<LocalDate> maxdate,
      @RequestParam(value = "subtype", required = false) Optional<ObjectSubtype> subtype,
      @RequestParam(value = "page", required = false) Optional<Integer> page
  ) {
    Stream<Map.Entry<String, Optional<String>>> stream = Stream.of(
        new AbstractMap.SimpleEntry<>("username", username),
        new AbstractMap.SimpleEntry<>("mindate", mindate.map(LOCALDATE_FORMATTER::format)),
        new AbstractMap.SimpleEntry<>("maxdate", maxdate.map(LOCALDATE_FORMATTER::format)),
        new AbstractMap.SimpleEntry<>("subtype", subtype.map(ObjectSubtype::value)),
        new AbstractMap.SimpleEntry<>("page", page.map(Object::toString))
    );
    Map<String, String> params = stream
        .filter(e -> e.getValue().isPresent())
        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().get()));
    ResponseEntity<Plays> response = bggPlaysService.getPlays(id, ObjectType.THING.value(), params);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

}
