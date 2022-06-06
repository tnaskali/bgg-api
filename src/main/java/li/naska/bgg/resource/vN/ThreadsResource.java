package li.naska.bgg.resource.vN;

import li.naska.bgg.resource.vN.model.Thread;
import li.naska.bgg.resource.vN.model.ThreadParams;
import li.naska.bgg.service.ForumsService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/vN/threads")
public class ThreadsResource {

  @Autowired
  private ForumsService forumsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Thread> getThread(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated ThreadParams params) {
    return forumsService.getThread(id, params);
  }

}
