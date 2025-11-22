package li.naska.bgg.resource.v2;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggCollectionV2Repository;
import li.naska.bgg.repository.model.BggCollectionV2QueryParams;
import li.naska.bgg.service.AuthenticationService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("collectionV2Resource")
@RequestMapping("/api/v2/collection")
public class CollectionResource {

  private final AuthenticationService authenticationService;

  private final BggCollectionV2Repository collectionRepository;

  public CollectionResource(
      BggCollectionV2Repository collectionRepository, AuthenticationService authenticationService) {
    this.collectionRepository = collectionRepository;
    this.authenticationService = authenticationService;
  }

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Collection",
      description = """
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
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc12"))
  public Mono<String> getCollection(
      @Validated @ParameterObject BggCollectionV2QueryParams params, ServerHttpRequest request) {
    return authenticationService.optionalAuthenticationCookieHeaderValue().flatMap(cookie -> {
      if (request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML)) {
        return collectionRepository.getItemsAsXml(cookie, params);
      } else {
        return collectionRepository.getItemsAsJson(cookie, params);
      }
    });
  }
}
