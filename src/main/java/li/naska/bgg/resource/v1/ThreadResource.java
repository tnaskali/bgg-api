package li.naska.bgg.resource.v1;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggThreadV1Repository;
import li.naska.bgg.repository.model.BggThreadV1QueryParams;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("ThreadV1Resource")
@RequestMapping("/api/v1/thread")
public class ThreadResource {

  @Autowired private BggThreadV1Repository threadRepository;

  @Deprecated
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
  @Operation(
      summary = "Retrieve the messages from a forum/game thread",
      description =
          """
          <b>This endpoint doesn't seem to be working anymore as it gets redirected to an invalid URI.</b>
          <p>
          Retrieve the messages from a forum/game thread.
          <p>
          <i>Note</i> : Your browser may interpret this as a request to subscribe to an RSS feed.
          <p>
          <i>Syntax</i> : /thread/{id}
          <p>
          <i>Example</i> : /thread/381021
          """,
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API#toc6"))
  public Mono<String> getThread(
      @NotNull @PathVariable @Parameter(description = "The thread id(s).", example = "381021")
          Integer id,
      @ParameterObject BggThreadV1QueryParams params) {
    return threadRepository.getThread(id, params);
  }
}
