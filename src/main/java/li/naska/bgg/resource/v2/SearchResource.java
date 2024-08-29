package li.naska.bgg.resource.v2;

import com.boardgamegeek.search.v2.Items;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggSearchV2Repository;
import li.naska.bgg.repository.model.BggSearchV2QueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("SearchV2Resource")
@RequestMapping("/api/v2/search")
public class SearchResource {

  @Autowired
  private BggSearchV2Repository searchRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Search",
      description =
          """
          You can search for items from the database by name.
          <p>
          <i>Syntax</i> : /search?query={queryString}[&{parameters}]
          <p>
          <i>Example</i> : /search?query=Crossbows+and+Catapults
          """,
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc14"))
  public Mono<String> getResults(
      @Validated @ParameterObject BggSearchV2QueryParams params, ServerHttpRequest request) {
    boolean keepXml = request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML);
    return searchRepository
        .getResults(params)
        .map(xml -> keepXml ? xml : xmlProcessor.toJsonString(xml, Items.class));
  }
}
