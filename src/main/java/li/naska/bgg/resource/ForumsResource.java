package li.naska.bgg.resource;

import com.boardgamegeek.enums.ObjectType;
import com.boardgamegeek.forum.Forum;
import com.boardgamegeek.forumlist.Forums;
import li.naska.bgg.service.ForumListsService;
import li.naska.bgg.service.ForumsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/forums")
public class ForumsResource {

  private static final DateTimeFormatter LOCALDATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Autowired
  private ForumsService forumsService;

  @Autowired
  private ForumListsService forumListsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Forums> getForum(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "type") ObjectType type
  ) {
    ResponseEntity<Forums> response = forumListsService.getForums(id, type);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

  @GetMapping(value = "/{id}/threads", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Forum> getThreads(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "page", required = false) Optional<Integer> page
  ) {
    Stream<Map.Entry<String, Optional<String>>> stream = Stream.of(
        new AbstractMap.SimpleEntry<>("page", page.map(Object::toString))
    );
    Map<String, String> params = stream
        .filter(e -> e.getValue().isPresent())
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    ResponseEntity<Forum> response = forumsService.getForum(id, params);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

}
