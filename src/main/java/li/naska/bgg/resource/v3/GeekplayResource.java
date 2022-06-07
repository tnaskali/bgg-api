package li.naska.bgg.resource.v3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggGeekplayV2Repository;
import li.naska.bgg.repository.model.BggGeekplayV3RequestBody;
import li.naska.bgg.repository.model.BggGeekplayV3ResponseBody;
import li.naska.bgg.service.AuthenticationService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v3/geekplay")
public class GeekplayResource {

  @Autowired
  private BggGeekplayV2Repository geekplayRepository;

  @Autowired
  private AuthenticationService authenticationService;

  @PostMapping(path = "/current", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayV3ResponseBody> updateCurrentGeekplay(@ParameterObject @Validated @RequestBody BggGeekplayV3RequestBody params) {
    return authenticationService.authentication().flatMap(
        authn -> geekplayRepository.updateGeekplay(authn.buildBggRequestHeader(), params));
  }

}
