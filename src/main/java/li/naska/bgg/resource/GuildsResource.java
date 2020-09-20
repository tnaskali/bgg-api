package li.naska.bgg.resource;

import com.boardgamegeek.enums.SortType;
import com.boardgamegeek.guild.Guild;
import li.naska.bgg.service.BggGuildsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/guilds")
public class GuildsResource {

  @Autowired
  private BggGuildsService bggGuildsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Guild> getGuild(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "members", required = false) Optional<Boolean> members,
      @RequestParam(value = "sort", required = false) Optional<SortType> sort,
      @RequestParam(value = "page", required = false) Optional<Integer> page
  ) {
    Stream<Map.Entry<String, Optional<String>>> stream = Stream.of(
        new AbstractMap.SimpleEntry<>("members", members.map(e -> e ? "1" : "0")),
        new AbstractMap.SimpleEntry<>("sort", sort.map(SortType::value)),
        new AbstractMap.SimpleEntry<>("page", page.map(Object::toString))
    );
    Map<String, String> params = stream
        .filter(e -> e.getValue().isPresent())
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    ResponseEntity<Guild> response = bggGuildsService.getGuild(id, params);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

}
