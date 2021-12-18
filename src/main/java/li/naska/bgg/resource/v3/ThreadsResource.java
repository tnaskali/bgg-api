package li.naska.bgg.resource.v3;

import com.boardgamegeek.thread.Thread;
import li.naska.bgg.resource.v3.model.ThreadParams;
import li.naska.bgg.service.ThreadsService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v3/threads")
public class ThreadsResource {

  @Autowired
  private ThreadsService threadsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Thread> getThread(@ParameterObject @Validated ThreadParams parameters) {
    return threadsService.getThread(parameters);
  }

}
