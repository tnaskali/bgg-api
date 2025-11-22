package li.naska.bgg.resource.v2;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggUserV2Repository;
import li.naska.bgg.repository.model.BggUserV2QueryParams;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("userV2Resource")
@RequestMapping("/api/v2/user")
public class UserResource {

  private final BggUserV2Repository userRepository;

  public UserResource(BggUserV2Repository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Users",
      description = """
          With the XMLAPI2 you can request basic public profile information about a user by username.
          <p>
          <i>Syntax</i> : /user?name={username}[&{parameters}]
          <p>
          <i>Example</i> : /user?name=eekspider
          """,
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc9"))
  public Mono<String> getUser(
      @Validated @ParameterObject BggUserV2QueryParams params, ServerHttpRequest request) {
    if (request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML)) {
      return userRepository.getUserAsXml(params);
    } else {
      return userRepository.getUserAsJson(params);
    }
  }
}
