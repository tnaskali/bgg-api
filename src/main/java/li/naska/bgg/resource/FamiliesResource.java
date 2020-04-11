package li.naska.bgg.resource;

import com.boardgamegeek.plays.ItemSubtypeEnum;
import com.boardgamegeek.plays.ItemTypeEnum;
import com.boardgamegeek.plays.Plays;
import li.naska.bgg.service.PlaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/families")
public class FamiliesResource {

  private static final DateTimeFormatter LOCALDATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Autowired
  private PlaysService playsService;

  @GetMapping(value = "/{id}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Plays> getPlays(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "username", required = false) Optional<String> username,
      @RequestParam(value = "mindate", required = false) Optional<LocalDate> mindate,
      @RequestParam(value = "maxdate", required = false) Optional<LocalDate> maxdate,
      @RequestParam(value = "subtype", required = false) Optional<ItemSubtypeEnum> subtype,
      @RequestParam(value = "page", required = false) Optional<Integer> page
  ) {
    Stream<Map.Entry<String, Optional<String>>> stream = Stream.of(
        new AbstractMap.SimpleEntry<>("mindate", mindate.map(LOCALDATETIME_FORMATTER::format)),
        new AbstractMap.SimpleEntry<>("maxdate", maxdate.map(LOCALDATETIME_FORMATTER::format)),
        new AbstractMap.SimpleEntry<>("subtype", subtype.map(ItemSubtypeEnum::value)),
        new AbstractMap.SimpleEntry<>("page", page.map(Object::toString))
    );
    Map<String, String> params = stream
        .filter(e -> e.getValue().isPresent())
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    ResponseEntity<Plays> response = playsService.getPlays(id, ItemTypeEnum.FAMILY.value(), params);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

}
