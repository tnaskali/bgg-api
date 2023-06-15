package li.naska.bgg.resource.v1;

import com.boardgamegeek.collection.v1.Items;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggCollectionV1Repository;
import li.naska.bgg.repository.model.BggCollectionV1QueryParams;
import li.naska.bgg.security.BggAuthenticationToken;
import li.naska.bgg.service.AuthenticationService;
import li.naska.bgg.util.XmlProcessor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("CollectionV1Resource")
@RequestMapping("/api/v1/collection")
public class CollectionResource {

  @Autowired
  private BggCollectionV1Repository collectionRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @Autowired
  private AuthenticationService authenticationService;

  @GetMapping(path = "/{username}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Retrieve games in a user's collection",
      description = """
          Retrieve games in a user's collection.
          <p>
          Important definition: A user's collection includes any games the user has added to her collection on BGG. This includes games she owns, games she used to own, games she's rated, games upon which she's commented, games she's played, and games on her wishlist, just to name a few. In this section, references to the collection mean this broader sense of BGG collection, not the user's personal stash of games.
          <p>
          <i>Syntax</i> : /collection/{username}[?{parameters}]
          <p>
          <i>Example</i> : /collection/eekspider
          """,
      security = @SecurityRequirement(name = "basicAuth"),
      externalDocs = @ExternalDocumentation(
          description = "original documentation",
          url = "https://boardgamegeek.com/wiki/page/BGG_XML_API#toc5"
      )
  )
  public Mono<String> getCollection(@PathVariable @Parameter(description = "username", example = "eekspider") String username,
                                    @Validated @ParameterObject BggCollectionV1QueryParams params,
                                    ServerHttpRequest request) {
    boolean keepXml = request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML);
    return authenticationService.optionalAuthentication()
        .flatMap(authn -> collectionRepository.getCollection(authn.map(BggAuthenticationToken::buildBggRequestHeader), username, params))
        .map(xml -> keepXml ? xml : xmlProcessor.toJsonString(xml, Items.class));
  }

}
