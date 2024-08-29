package li.naska.bgg.resource.v3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggGeekitempollV3Repository;
import li.naska.bgg.repository.model.BggGeekitempollV3QueryParams;
import li.naska.bgg.repository.model.BggGeekitempollV3ResponseBody;
import li.naska.bgg.security.BggAuthenticationToken;
import li.naska.bgg.service.AuthenticationService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("GeekitempollV3Resource")
@RequestMapping("/api/v3/geekitempoll")
public class GeekitempollResource {

  @Autowired private BggGeekitempollV3Repository geekitempollRepository;

  @Autowired private AuthenticationService authenticationService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get item poll",
      description = "Get poll information for a given item.",
      security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekitempollV3ResponseBody> getGeekitempoll(
      @Validated @ParameterObject BggGeekitempollV3QueryParams params) {
    return authenticationService
        .optionalAuthentication()
        .flatMap(
            authn ->
                geekitempollRepository.getGeekitempoll(
                    authn.map(BggAuthenticationToken::buildBggRequestHeader), params));
  }
}
