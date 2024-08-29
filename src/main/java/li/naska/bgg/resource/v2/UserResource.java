package li.naska.bgg.resource.v2;

import com.boardgamegeek.user.v2.User;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggUserV2Repository;
import li.naska.bgg.repository.model.BggUserV2QueryParams;
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

@RestController("UserV2Resource")
@RequestMapping("/api/v2/user")
public class UserResource {

  @Autowired
  private BggUserV2Repository usersRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Users",
      description =
          """
          With the XMLAPI2 you can request basic public profile information about a user by username.
          <p>
          <i>Syntax</i> : /user?name={username}[&{parameters}]
          <p>
          <i>Example</i> : /user?name=eekspider
          """,
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_XML_API2#toc8"))
  public Mono<String> getUser(
      @Validated @ParameterObject BggUserV2QueryParams params, ServerHttpRequest request) {
    boolean keepXml = request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML);
    return usersRepository
        .getUser(params)
        .map(xml -> keepXml ? xml : xmlProcessor.toJsonString(xml, User.class));
  }
}
