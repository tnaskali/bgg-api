package li.naska.bgg.resource.v2;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggPlaysV2Repository;
import li.naska.bgg.repository.model.BggPlaysV2QueryParams;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("playsV2Resource")
@RequestMapping("/api/v2/plays")
public class PlaysResource {

  private final BggPlaysV2Repository playsRepository;

  public PlaysResource(BggPlaysV2Repository playsRepository) {
    this.playsRepository = playsRepository;
  }

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Plays",
      description = """
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
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc11"))
  public Mono<String> getPlays(
      @Validated @ParameterObject BggPlaysV2QueryParams params, ServerHttpRequest request) {
    if (request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML)) {
      return playsRepository.getPlaysAsXml(params);
    } else {
      return playsRepository.getPlaysAsJson(params);
    }
  }
}
