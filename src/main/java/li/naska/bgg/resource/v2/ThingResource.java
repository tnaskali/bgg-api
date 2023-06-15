package li.naska.bgg.resource.v2;

import com.boardgamegeek.thing.v2.Items;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggThingV2Repository;
import li.naska.bgg.repository.model.BggThingV2QueryParams;
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

@RestController("ThingV2Resource")
@RequestMapping("/api/v2/thing")
public class ThingResource {

  @Autowired
  private BggThingV2Repository thingsRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Thing Items",
      description = """
          In the BGG database, any physical, tangible product is called a thing.
          <p>
          <i>Syntax</i> : /thing?id={ids}[&{parameters}]
          <p>
          <i>Example</i> : /thing?id=383974
          """,
      externalDocs = @ExternalDocumentation(
          description = "original documentation",
          url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc3"
      )
  )
  public Mono<String> getThings(@Validated @ParameterObject BggThingV2QueryParams params,
                                ServerHttpRequest request) {
    boolean keepXml = request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML);
    return thingsRepository.getThings(params)
        .map(xml -> keepXml ? xml : xmlProcessor.toJsonString(xml, Items.class));
  }

}
