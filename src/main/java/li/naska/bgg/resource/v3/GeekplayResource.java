package li.naska.bgg.resource.v3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggGeekplayV3Repository;
import li.naska.bgg.repository.model.BggGeekplayV3RequestBody;
import li.naska.bgg.repository.model.BggGeekplayV3RequestParams;
import li.naska.bgg.repository.model.BggGeekplayV3ResponseBody;
import li.naska.bgg.service.AuthenticationService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController("GeekplayV3Resource")
@RequestMapping("/api/v3/geekplay")
public class GeekplayResource {

  @Autowired
  private BggGeekplayV3Repository geekplayRepository;

  @Autowired
  private AuthenticationService authenticationService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get play details",
      description = "Get play details.",
      security = @SecurityRequirement(name = "basicAuth")
  )
  public Mono<BggGeekplayV3ResponseBody> getGeekplay(@Validated @ParameterObject BggGeekplayV3RequestParams params) {
    return authenticationService.requiredAuthentication().flatMap(
        authn -> geekplayRepository.getGeekplay(authn.buildBggRequestHeader(), params));
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Manages plays of the current user",
      description = "Manages plays of the current user.",
      security = @SecurityRequirement(name = "basicAuth")
  )
  public Mono<BggGeekplayV3ResponseBody> updateGeekplay(@Validated @RequestBody BggGeekplayV3RequestBody body) {
    return authenticationService.requiredAuthentication().flatMap(
        authn -> geekplayRepository.updateGeekplay(authn.buildBggRequestHeader(), body));
  }

}
