package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggThreadsV4Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get thread",
      description = """
          Get thread by id.
          <p>
          <i>Syntax</i> : /threads/{id}
          <p>
          <i>Example</i> : /threads/2869195
          """
  )
  public Mono<String> getThread(@NotNull @PathVariable @Parameter(example = "2869195", description = "Thread id.") Integer id) {
    return threadsRepository.getThread(id);
  }

}
