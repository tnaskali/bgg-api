package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggFansV4Repository;
import li.naska.bgg.repository.model.BggFansV4QueryParams;
import li.naska.bgg.repository.model.BggFansV4RequestBody;
import li.naska.bgg.repository.model.BggFansV4ResponseBody;
import li.naska.bgg.service.AuthenticationService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController("FansV4Resource")
@RequestMapping("/api/v4/fans")
public class FansResource {

  private final BggFansV4Repository fansRepository;

  private final AuthenticationService authenticationService;

  public FansResource(
      BggFansV4Repository fansRepository, AuthenticationService authenticationService) {
    this.fansRepository = fansRepository;
    this.authenticationService = authenticationService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get fans",
      description =
          """
          Get fans information for a given object.
          <p>
          <i>Syntax</i> : /fans?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /fans?objectid=1000&objecttype=thing
          """)
  public Mono<BggFansV4ResponseBody> getFans(
      @Validated @ParameterObject BggFansV4QueryParams params) {
    return fansRepository.getFans(params);
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Add self to fans",
      description =
          """
          Adds the current user to the fans.
          <p>
          <i>Syntax</i> : /fans/{id}
          <p>
          <i>Example</i> : /fans/{12345}
          """,
      security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggFansV4ResponseBody> postFan(@Validated @RequestBody BggFansV4RequestBody body) {
    return authenticationService
        .requiredAuthentication()
        .flatMap(authn -> fansRepository.createFan(authn.buildBggRequestHeader(), body));
  }

  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Delete self from fans",
      description =
          """
          Delete the current user from the fans.
          <p>
          <i>Syntax</i> : /fans/{id}
          <p>
          <i>Example</i> : /fans/{12345}
          """,
      security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggFansV4ResponseBody> deleteFan(
      @Validated @PathVariable @Parameter(example = "12345", description = "Fan id.") Integer id) {
    return authenticationService
        .requiredAuthentication()
        .flatMap(authn -> fansRepository.deleteFan(authn.buildBggRequestHeader(), id));
  }
}
