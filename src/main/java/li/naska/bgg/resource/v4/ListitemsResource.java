package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggListitemsV4Repository;
import li.naska.bgg.repository.model.*;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("listitemsV4Resource")
@RequestMapping("/api/v4/listitems")
public class ListitemsResource {

  private final BggListitemsV4Repository listitemsRepository;

  public ListitemsResource(BggListitemsV4Repository listitemsRepository) {
    this.listitemsRepository = listitemsRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get listitems", description = """
          Get listitems by geeklist id.
          <p>
          <i>Syntax</i> : /listitems?listid={listid}[&{parameters}]
          <p>
          <i>Example</i> : /listitems?listid=250030
          """)
  public Mono<BggListitemsV4ResponseBody> getListitems(
      @Validated @ParameterObject BggListitemsV4QueryParams params) {
    return listitemsRepository.getListitems(params);
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get listitem", description = """
          Get listitem by id.
          <p>
          <i>Syntax</i> : /listitems/{id}
          <p>
          <i>Example</i> : /listitems/6632367
          """)
  public Mono<BggListitemV4ResponseBody> getListitem(
      @NotNull @PathVariable @Parameter(example = "6632367", description = "The listitem id.")
          Integer id) {
    return listitemsRepository.getListitem(id);
  }

  @GetMapping(path = "/{id}/reactions", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get listitem reactions", description = """
          Get reactions for a given listitem.
          <p>
          <i>Syntax</i> : /listitems/{id}/reactions[?{parameters}]
          <p>
          <i>Example</i> : /listitems/6632367/reactions
          """)
  public Mono<BggListitemReactionsV4ResponseBody> getListitemReactions(
      @NotNull @PathVariable @Parameter(example = "6632367", description = "The listitem id.")
          Integer id,
      @Validated @ParameterObject BggListitemReactionsV4QueryParams params) {
    return listitemsRepository.getListitemReactions(id, params);
  }

  @GetMapping(path = "/{id}/tips", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get listitem tips", description = """
          Get tips for a given listitem.
          <p>
          <i>Syntax</i> : /listitems/{id}/tips[?{parameters}]
          <p>
          <i>Example</i> : /listitems/6632367/tips
          """)
  public Mono<BggListitemTipsV4ResponseBody> getListitemTips(
      @NotNull @PathVariable @Parameter(example = "6632367", description = "The listitem id.")
          Integer id,
      @Validated @ParameterObject BggListitemTipsV4QueryParams params) {
    return listitemsRepository.getListitemTips(id, params);
  }

  @GetMapping(path = "/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get listitem comments", description = """
          Get comments for a given listitem.
          <p>
          <i>Syntax</i> : /listitems/{id}/comments[?{parameters}]
          <p>
          <i>Example</i> : /listitems/6632367/comments
          """)
  public Mono<BggListitemCommentsV4ResponseBody> getListitemComments(
      @NotNull @PathVariable @Parameter(example = "6632367", description = "The listitem id.")
          Integer id,
      @Validated @ParameterObject BggListitemCommentsV4QueryParams params) {
    return listitemsRepository.getListitemComments(id, params);
  }
}
