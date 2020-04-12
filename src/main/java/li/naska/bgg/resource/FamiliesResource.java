package li.naska.bgg.resource;

import com.boardgamegeek.family.Families;
import com.boardgamegeek.family.FamilyType;
import com.boardgamegeek.plays.PlayItemSubtype;
import com.boardgamegeek.plays.PlayItemType;
import com.boardgamegeek.plays.Plays;
import li.naska.bgg.service.FamiliesService;
import li.naska.bgg.service.PlaysService;
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
@RequestMapping("/api/v1/families")
public class FamiliesResource {

  private static final DateTimeFormatter LOCALDATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Autowired
  private PlaysService playsService;

  @Autowired
  private FamiliesService familiesService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Families> getFamily(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "types", required = false) Optional<List<FamilyType>> types
  ) {
    Stream<Map.Entry<String, Optional<String>>> stream = Stream.of(
        new AbstractMap.SimpleEntry<>("type", types.map(l -> l.stream().map(FamilyType::value).collect(Collectors.joining(","))))
    );
    Map<String, String> params = stream
        .filter(e -> e.getValue().isPresent())
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    ResponseEntity<Families> response = familiesService.getFamily(id, params);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

  @GetMapping(value = "/{id}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Plays> getPlays(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "username", required = false) Optional<String> username,
      @RequestParam(value = "mindate", required = false) Optional<LocalDate> mindate,
      @RequestParam(value = "maxdate", required = false) Optional<LocalDate> maxdate,
      @RequestParam(value = "subtype", required = false) Optional<PlayItemSubtype> subtype,
      @RequestParam(value = "page", required = false) Optional<Integer> page
  ) {
    Stream<Map.Entry<String, Optional<String>>> stream = Stream.of(
        new AbstractMap.SimpleEntry<>("mindate", mindate.map(LOCALDATE_FORMATTER::format)),
        new AbstractMap.SimpleEntry<>("maxdate", maxdate.map(LOCALDATE_FORMATTER::format)),
        new AbstractMap.SimpleEntry<>("subtype", subtype.map(PlayItemSubtype::value)),
        new AbstractMap.SimpleEntry<>("page", page.map(Object::toString))
    );
    Map<String, String> params = stream
        .filter(e -> e.getValue().isPresent())
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    ResponseEntity<Plays> response = playsService.getPlays(id, PlayItemType.FAMILY.value(), params);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

}
