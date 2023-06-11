package li.naska.bgg.resource.v1;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggSearchV1Repository;
import li.naska.bgg.repository.model.BggSearchV1QueryParams;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("SearchV1Resource")
@RequestMapping("/api/v1/search")
public class SearchResource {

  @Autowired
  private BggSearchV1Repository searchRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  @Operation(
      summary = "Search for games by name and by AKAs",
      description = """
          Search for games by name and by AKAs.
          <p>
          <i>Syntax</i> : /search?search={searchString}[&{parameters}]
          <p>
          <i>Example</i> : /search?search=Crossbows%20and%20Catapults
          """,
      externalDocs = @ExternalDocumentation(
          description = "original documentation",
          url = "https://boardgamegeek.com/wiki/page/BGG_XML_API#toc3"
      )
  )
  public Mono<String> getResults(@Validated @ParameterObject BggSearchV1QueryParams params) {
    return searchRepository.getResults(params);
  }

}
