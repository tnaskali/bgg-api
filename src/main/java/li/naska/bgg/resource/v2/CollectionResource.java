package li.naska.bgg.resource.v2;

import com.boardgamegeek.collection.v2.Items;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggCollectionV2Repository;
import li.naska.bgg.repository.model.BggCollectionV2QueryParams;
import li.naska.bgg.security.BggAuthenticationToken;
import li.naska.bgg.service.AuthenticationService;
import li.naska.bgg.util.XmlProcessor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("CollectionV2Resource")
@RequestMapping("/api/v2/collection")
public class CollectionResource {

  private final AuthenticationService authenticationService;

  private final BggCollectionV2Repository collectionRepository;

  private final XmlProcessor xmlProcessor;

  public CollectionResource(
      AuthenticationService authenticationService,
      BggCollectionV2Repository collectionRepository,
      XmlProcessor xmlProcessor) {
    this.collectionRepository = collectionRepository;
    this.authenticationService = authenticationService;
    this.xmlProcessor = xmlProcessor;
  }

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Collection",
      description =
          """
          Request details about a user's collection.
          <p>
          <i>Syntax</i> : /collection?username={username}[&{parameters}]
          <p>
          <i>Example</i> : /collection?username=eekspider
          """,
      security = @SecurityRequirement(name = "basicAuth"),
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc11"))
  public Mono<String> getCollection(
      @Validated @ParameterObject BggCollectionV2QueryParams params, ServerHttpRequest request) {
    boolean keepXml = request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML);
    return authenticationService
        .optionalAuthentication()
        .flatMap(authn -> collectionRepository.getCollection(
            authn.map(BggAuthenticationToken::buildBggRequestHeader), params))
        .map(xml -> keepXml ? xml : xmlProcessor.toJsonString(xml, Items.class));
  }
}
