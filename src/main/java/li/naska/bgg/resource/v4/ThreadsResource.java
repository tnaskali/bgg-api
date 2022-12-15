package li.naska.bgg.resource.v4;

import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggForumsV4Repository;
import li.naska.bgg.repository.BggThreadsV4Repository;
import li.naska.bgg.repository.model.BggThreadsV4QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("ThreadsV4Resource")
@RequestMapping("/api/v4/threads")
public class ThreadsResource {

  @Autowired
  private BggThreadsV4Repository threadsRepository;

  @Autowired
  private BggForumsV4Repository forumsRepository;

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getThread(@NotNull @PathVariable Integer id) {
    return threadsRepository.getThread(id);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getThreads(@Validated BggThreadsV4QueryParams params) {
    return forumsRepository.getThreads(params);
  }

}
