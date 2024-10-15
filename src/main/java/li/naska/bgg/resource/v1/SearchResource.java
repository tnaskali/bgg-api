package li.naska.bgg.resource.v1;

import com.boardgamegeek.search.v1.Boardgames;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggSearchV1Repository;
import li.naska.bgg.repository.model.BggSearchV1QueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("SearchV1Resource")
@RequestMapping("/api/v1/search")
public class SearchResource {

  private final BggSearchV1Repository searchRepository;

  private final XmlProcessor xmlProcessor;

  public SearchResource(BggSearchV1Repository searchRepository, XmlProcessor xmlProcessor) {
    this.searchRepository = searchRepository;
    this.xmlProcessor = xmlProcessor;
  }

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Search for games by name and by AKAs",
      description =
          """
          Search for games by name and by AKAs.
          <p>
          <i>Syntax</i> : /search?search={searchString}[&{parameters}]
          <p>
          <i>Example</i> : /search?search=Crossbows%20and%20Catapults
          """,
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API#toc3"))
  public Mono<String> getResults(
      @Validated @ParameterObject BggSearchV1QueryParams params, ServerHttpRequest request) {
    boolean keepXml = request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML);
    return searchRepository
        .getResults(params)
        .map(xml -> keepXml ? xml : xmlProcessor.toJsonString(xml, Boardgames.class));
  }
}
