package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggGeeklistsV4Repository;
import li.naska.bgg.repository.model.*;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("geeklistsV4Resource")
@RequestMapping("/api/v4/geeklists")
public class GeeklistsResource {

  private final BggGeeklistsV4Repository geeklistsRepository;

  public GeeklistsResource(BggGeeklistsV4Repository geeklistsRepository) {
    this.geeklistsRepository = geeklistsRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get geeklists", description = """
          Get geeklists by object id and type.
          <p>
          <i>Syntax</i> : /geeklists?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /geeklists?objectid=1000&objecttype=thing
          """)
  public Mono<BggGeeklistsV4ResponseBody> getGeeklists(
      @Validated @ParameterObject BggGeeklistsV4QueryParams params) {
    return geeklistsRepository.getGeeklists(params);
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get geeklist", description = """
          Get geeklist by id.
          <p>
          <i>Syntax</i> : /geeklists/{id}
          <p>
          <i>Example</i> : /geeklists/250030
          """)
  public Mono<BggGeeklistV4ResponseBody> getGeeklist(
      @NotNull @PathVariable @Parameter(example = "250030", description = "Geeklist id.")
          Integer id) {
    return geeklistsRepository.getGeeklist(id);
  }

  @GetMapping(path = "/{id}/reactions", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get geeklist reactions", description = """
          Get reactions for a given geeklist.
          <p>
          <i>Syntax</i> : /geeklists/{id}/reactions[?{parameters}]
          <p>
          <i>Example</i> : /geeklists/250030/reactions
          """)
  public Mono<BggGeeklistReactionsV4ResponseBody> getGeeklistReactions(
      @NotNull @PathVariable @Parameter(example = "250030", description = "Geeklist id.")
          Integer id,
      @Validated @ParameterObject BggGeeklistReactionsV4QueryParams params) {
    return geeklistsRepository.getGeeklistReactions(id, params);
  }

  @GetMapping(path = "/{id}/tips", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get geeklist tips", description = """
          Get tips for a given geeklist.
          <p>
          <i>Syntax</i> : /geeklists/{id}/tips[?{parameters}]
          <p>
          <i>Example</i> : /geeklists/250030/tips
          """)
  public Mono<BggGeeklistTipsV4ResponseBody> getGeeklistTips(
      @NotNull @PathVariable @Parameter(example = "250030", description = "Geeklist id.")
          Integer id,
      @Validated @ParameterObject BggGeeklistTipsV4QueryParams params) {
    return geeklistsRepository.getGeeklistTips(id, params);
  }

  @GetMapping(path = "/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get geeklist comments", description = """
          Get comments for a given geeklist.
          <p>
          <i>Syntax</i> : /geeklists/{id}/comments[?{parameters}]
          <p>
          <i>Example</i> : /geeklists/250030/comments
          """)
  public Mono<BggGeeklistCommentsV4ResponseBody> getGeeklistComments(
      @NotNull @PathVariable @Parameter(example = "250030", description = "Geeklist id.")
          Integer id,
      @Validated @ParameterObject BggGeeklistCommentsV4QueryParams params) {
    return geeklistsRepository.getGeeklistComments(id, params);
  }
}
