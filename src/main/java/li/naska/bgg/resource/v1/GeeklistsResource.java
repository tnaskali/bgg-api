package li.naska.bgg.resource.v1;

import com.boardgamegeek.geeklist.Geeklist;
import li.naska.bgg.service.BggGeeklistsService;
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
@RequestMapping("/api/v1/geeklists")
public class GeeklistsResource {

  @Autowired
  private BggGeeklistsService bggGeeklistsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Geeklist> getGeeklist(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "comments", required = false) Optional<Boolean> comments
  ) {
    Stream<Map.Entry<String, Optional<String>>> stream = Stream.of(
        new AbstractMap.SimpleEntry<>("comments", comments.map(e -> e ? "1" : "0"))
    );
    Map<String, String> params = stream
        .filter(e -> e.getValue().isPresent())
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    ResponseEntity<Geeklist> response = bggGeeklistsService.getGeeklist(id, params);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

}
