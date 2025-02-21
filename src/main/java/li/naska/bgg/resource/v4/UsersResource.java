package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggUsersV4Repository;
import li.naska.bgg.repository.model.BggCurrentUserV4ResponseBody;
import li.naska.bgg.repository.model.BggUserV4ResponseBody;
import li.naska.bgg.repository.model.BggUsersV4QueryParams;
import li.naska.bgg.service.AuthenticationService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("usersV4Resource")
@RequestMapping("/api/v4/users")
public class UsersResource {

  private final BggUsersV4Repository usersRepository;

  private final AuthenticationService authenticationService;

  public UsersResource(
      BggUsersV4Repository usersRepository, AuthenticationService authenticationService) {
    this.usersRepository = usersRepository;
    this.authenticationService = authenticationService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get user by username",
      description =
          """
          Get user by username.
          <p>
          <i>Syntax</i> : /users?username={username}
          <p>
          <i>Example</i> : /users?username=Jester
          """)
  public Mono<BggUserV4ResponseBody> getUsers(
      @Validated @ParameterObject BggUsersV4QueryParams params) {
    return usersRepository.getUser(params);
  }

  @GetMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get current user",
      description =
          """
          Get current user.
          <p>
          <i>Syntax</i> : /users/current
          <p>
          <i>Example</i> : /users/current
          """,
      security = @SecurityRequirement(name = "basicAuth"),
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_JSON_API#toc3"))
  public Mono<BggCurrentUserV4ResponseBody> getCurrentUser() {
    return authenticationService
        .optionalAuthenticationCookieHeaderValue()
        .flatMap(usersRepository::getCurrentUser);
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get user by id",
      description =
          """
          Get user by id.
          <p>
          <i>Syntax</i> : /users/{id}
          <p>
          <i>Example</i> : /users/10937
          """,
      externalDocs =
          @ExternalDocumentation(
              description = "original documentation",
              url = "https://boardgamegeek.com/wiki/page/BGG_JSON_API#toc4"))
  public Mono<BggUserV4ResponseBody> getUser(
      @PathVariable @Parameter(example = "10937", description = "User id.") Integer id) {
    return usersRepository.getUser(id);
  }
}
