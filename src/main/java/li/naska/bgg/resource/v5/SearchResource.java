package li.naska.bgg.resource.v5;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggSearchV5Repository;
import li.naska.bgg.repository.model.BggSearchV5QueryParams;
import li.naska.bgg.repository.model.BggSearchV5ResponseBody;
import li.naska.bgg.resource.v5.model.SearchContext;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("searchV5Resource")
@RequestMapping("/api/v5/search")
public class SearchResource {

  private final BggSearchV5Repository searchRepository;

  public SearchResource(BggSearchV5Repository searchRepository) {
    this.searchRepository = searchRepository;
  }

  @GetMapping(path = "/{context}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Quick search",
      description =
          """
          Quick item search.
          <p>
          <i>Syntax</i> : /search/{context}?q={query}[&{parameters}]
          <p>
          <i>Example</i> : /search/boardgame?q=corona
          """)
  public Mono<BggSearchV5ResponseBody> getSearchResults(
      @NotNull @PathVariable @Parameter(description = "Search context.") SearchContext context,
      @Validated @ParameterObject BggSearchV5QueryParams params) {
    return searchRepository.getSearchResults(context, params);
  }
}
