package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggForumsV4Repository;
import li.naska.bgg.repository.model.BggForumsThreadsV4QueryParams;
import li.naska.bgg.repository.model.BggForumsThreadsV4ResponseBody;
import li.naska.bgg.repository.model.BggForumsV4QueryParams;
import li.naska.bgg.repository.model.BggForumsV4ResponseBody;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("ForumsV4Resource")
@RequestMapping("/api/v4/forums")
public class ForumsResource {

  @Autowired
  private BggForumsV4Repository forumsRepository;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get forums",
      description =
          """
          Get forums information for a given object.
          <p>
          <i>Syntax</i> : /forums?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /forums?objectid=205637&objecttype=thing
          """)
  public Mono<BggForumsV4ResponseBody> getForums(
      @Validated @ParameterObject BggForumsV4QueryParams params) {
    return forumsRepository.getForums(params);
  }

  @GetMapping(path = "/threads", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get threads",
      description =
          """
          Get thread for a given object id and type.
          <p>
          <i>Syntax</i> : /forums/threads?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /forums/threads?objectid=205637&objecttype=thing
          """)
  public Mono<BggForumsThreadsV4ResponseBody> getThreads(
      @Validated @ParameterObject BggForumsThreadsV4QueryParams params) {
    return forumsRepository.getThreads(params);
  }
}
