package li.naska.bgg.resource.v2;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggHotV2Repository;
import li.naska.bgg.repository.model.BggHotV2QueryParams;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("hotV2Resource")
@RequestMapping("/api/v2/hot")
public class HotResource {

  private final BggHotV2Repository hotRepository;

  public HotResource(BggHotV2Repository hotRepository) {
    this.hotRepository = hotRepository;
  }

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Hot Items",
      description =
          """
          You can retrieve the list of most active items on the site.
          <p>
          <i>Syntax</i> : /hot?type={type}
          <p>
          <i>Example</i> : /hot?type=boardgame
          """,
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc12"))
  public Mono<String> getHotItems(
      @Validated @ParameterObject BggHotV2QueryParams params, ServerHttpRequest request) {
    if (request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML)) {
      return hotRepository.getItemsAsXml(params);
    } else {
      return hotRepository.getItemsAsJson(params);
    }
  }
}
