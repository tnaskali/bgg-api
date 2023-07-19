package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggThreadsV4Repository;
import li.naska.bgg.repository.model.BggThreadReactionsV4QueryParams;
import li.naska.bgg.repository.model.BggThreadReactionsV4ResponseBody;
import li.naska.bgg.repository.model.BggThreadV4ResponseBody;
import org.springdoc.core.annotations.ParameterObject;
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

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get thread",
      description = """
          Get thread by id.
          <p>
          <i>Syntax</i> : /threads/{id}
          <p>
          <i>Example</i> : /threads/99401
          """
  )
  public Mono<BggThreadV4ResponseBody> getThread(@NotNull @PathVariable @Parameter(example = "99401", description = "Thread id.") Integer id) {
    return threadsRepository.getThread(id);
  }

  @GetMapping(path = "/{id}/reactions", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get thread reactions",
      description = """
          Get reactions for a given thread.
          <p>
          <i>Syntax</i> : /threads/{id}/reactions[?{parameters}]
          <p>
          <i>Example</i> : /threads/99401/reactions
          """
  )
  public Mono<BggThreadReactionsV4ResponseBody> getThreadReactions(@NotNull @PathVariable @Parameter(example = "99401", description = "Thread id.") Integer id,
                                                                   @Validated @ParameterObject BggThreadReactionsV4QueryParams params) {
    return threadsRepository.getThreadReactions(id, params);
  }

}
