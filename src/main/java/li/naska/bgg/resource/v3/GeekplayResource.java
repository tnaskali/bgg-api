package li.naska.bgg.resource.v3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggGeekplayV3Repository;
import li.naska.bgg.repository.model.*;
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
      summary = "Get plays details",
      description = """
          Get plays details.
          <p>
          <i>Syntax</i> : /geekplay?action={action}&objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /geekplay?action=getplays&objectid=205637&objecttype=thing
          """
  )
  public Mono<BggGeekplayPlaysV3ResponseBody> getGeekplayPlays(@Validated @ParameterObject BggGeekplayPlaysV3QueryParams params) {
    return geekplayRepository.getGeekplayPlays(params);
  }

  @GetMapping(path = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get play count information for the current user",
      description = """
          Get play count information for the current user.
          <p>
          <i>Syntax</i> : /geekplay/count?action=getuserplaycount[?{parameters}]
          <p>
          <i>Example</i> : /geekplay/count?action=getuserplaycount&userid=825923&objectid=205637&objecttype=thing
          """,
      security = @SecurityRequirement(name = "basicAuth")
  )
  public Mono<BggGeekplayCountV3ResponseBody> getGeekplayCount(@Validated @ParameterObject BggGeekplayCountV3QueryParams params) {
    return authenticationService.requiredAuthentication().flatMap(
        authn -> geekplayRepository.getGeekplayCount(authn.buildBggRequestHeader(), params));
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Updates play information for the current user",
      description = "Updates play information for the current user.",
      security = @SecurityRequirement(name = "basicAuth")
  )
  public Mono<BggGeekplayV3ResponseBody> updateGeekplay(@Validated @RequestBody BggGeekplayV3RequestBody body) {
    return authenticationService.requiredAuthentication().flatMap(
        authn -> geekplayRepository.updateGeekplay(authn.buildBggRequestHeader(), body));
  }

}
