package li.naska.bgg.resource.v1;

import com.boardgamegeek.thread.Thread;
import li.naska.bgg.repository.BggThreadsRepository;
import li.naska.bgg.repository.model.BggThreadsParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/v1/threads")
public class ThreadsResource {

  @Autowired
  private BggThreadsRepository bggThreadsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Thread> getThread(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "minarticleid", required = false) Integer minarticleid,
      @RequestParam(value = "minarticledate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate minarticledate,
      @RequestParam(value = "minarticletime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime minarticletime,
      @RequestParam(value = "count", required = false) Integer count
  ) {
    BggThreadsParameters parameters = new BggThreadsParameters(id);
    parameters.setMinarticleid(minarticleid);
    parameters.setMinarticledate(minarticledate);
    parameters.setMinarticletime(minarticletime);
    parameters.setCount(count);
    return bggThreadsService.getThread(parameters);
  }

}
