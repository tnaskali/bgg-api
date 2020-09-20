package li.naska.bgg.resource;

import com.boardgamegeek.enums.FamilyType;
import com.boardgamegeek.enums.ObjectSubtype;
import com.boardgamegeek.enums.ObjectType;
import com.boardgamegeek.family.Families;
import com.boardgamegeek.plays.Plays;
import li.naska.bgg.service.BggFamiliesService;
import li.naska.bgg.service.BggPlaysService;
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
  private BggPlaysService bggPlaysService;

  @Autowired
  private BggFamiliesService bggFamiliesService;

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
    ResponseEntity<Families> response = bggFamiliesService.getFamily(id, params);
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
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    ResponseEntity<Plays> response = bggPlaysService.getPlays(id, ObjectType.FAMILY.value(), params);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

}
