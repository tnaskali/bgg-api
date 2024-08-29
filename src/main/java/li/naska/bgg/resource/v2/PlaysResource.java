package li.naska.bgg.resource.v2;

import com.boardgamegeek.plays.v2.Plays;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggPlaysV2Repository;
import li.naska.bgg.repository.model.BggPlaysV2QueryParams;
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

@RestController("PlaysV2Resource")
@RequestMapping("/api/v2/plays")
public class PlaysResource {

  @Autowired
  private BggPlaysV2Repository playsRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Plays",
      description =
          """
          Request plays logged by a particular user or for a particular item. Data is returned in backwards-chronological form.
          <p>
          You must include either a username or an id and type to get results.
          <p>
          <i>Syntax</i> : /plays?username={username}[&{parameters}]
          <p>
          <i>Syntax</i> : /plays?id={itemId}&type={itemType}[&{parameters}]
          <p>
          <i>Example</i> : /plays?username=eekspider
          <p>
          <i>Example</i> : /plays?id=3085&type=thing
          """,
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc10"))
  public Mono<String> getPlays(
      @Validated @ParameterObject BggPlaysV2QueryParams params, ServerHttpRequest request) {
    boolean keepXml = request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML);
    return playsRepository
        .getPlays(params)
        .map(xml -> keepXml ? xml : xmlProcessor.toJsonString(xml, Plays.class));
  }
}
