package li.naska.bgg.resource;

import com.boardgamegeek.thread.Thread;
import li.naska.bgg.service.ThreadsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/threads")
public class ThreadsResource {

  private static final DateTimeFormatter LOCALDATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private static final DateTimeFormatter LOCALDATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'%20'HH'%3A'mm'%3A'ss");

  @Autowired
  private ThreadsService threadsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Thread> getThread(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "minarticleid", required = false) Optional<Integer> minarticleid,
      @RequestParam(value = "minarticledate", required = false) Optional<LocalDate> minarticledate,
      @RequestParam(value = "minarticledatetime", required = false) Optional<LocalDateTime> minarticledatetime,
      @RequestParam(value = "count", required = false) Optional<Integer> count
  ) {
    Stream<Map.Entry<String, Optional<String>>> stream = Stream.of(
        new AbstractMap.SimpleEntry<>("minarticleid", minarticleid.map(Object::toString)),
        new AbstractMap.SimpleEntry<>("minarticledate", minarticledatetime.isPresent() ? minarticledatetime.map(LOCALDATETIME_FORMATTER::format) : minarticledate.map(LOCALDATE_FORMATTER::format)),
        new AbstractMap.SimpleEntry<>("count", count.map(Object::toString))
    );
    Map<String, String> params = stream
        .filter(e -> e.getValue().isPresent())
        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get()));
    ResponseEntity<Thread> response = threadsService.getThread(id, params);
    return new ResponseEntity<>(response.getBody(), response.getStatusCode());
  }

}
