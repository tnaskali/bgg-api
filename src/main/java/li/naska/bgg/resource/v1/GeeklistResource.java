package li.naska.bgg.resource.v1;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggGeeklistV1Repository;
import li.naska.bgg.repository.model.BggGeeklistV1QueryParams;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("geeklistV1Resource")
@RequestMapping("/api/v1/geeklist")
public class GeeklistResource {

  private final BggGeeklistV1Repository geeklistRepository;

  public GeeklistResource(BggGeeklistV1Repository geeklistRepository) {
    this.geeklistRepository = geeklistRepository;
  }

  @GetMapping(
      value = "/{id}",
      produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Retrieve entries from a geeklist",
      description =
          """
          Retrieve entries from a geeklist.
          <p>
          <i>Note</i> : the old "start" and "count" parameters are obsolete and no longer supported. They were required in the original api if you wanted to retreive all items on a geeklist longer than 150 items as that's the most that could be returned on a single api call. Currently however the geeklist xml api returns the entire geeklist (all items) in a single call.
          <p>
          <i>Syntax</i> : /geeklist/{id}[?{parameters}]
          <p>
          <i>Example</i> : /geeklist/11205
          """,
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API#toc7"))
  public Mono<String> getGeeklist(
      @NotNull @PathVariable @Parameter(description = "The geeklist id.", example = "11205")
          Integer id,
      @Validated @ParameterObject BggGeeklistV1QueryParams params,
      ServerHttpRequest request) {
    if (request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML)) {
      return geeklistRepository.getGeeklistAsXml(id, params);
    } else {
      return geeklistRepository.getGeeklistAsJson(id, params);
    }
  }
}
