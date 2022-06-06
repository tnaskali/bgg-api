package li.naska.bgg.resource.v2;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggGeekplaysRepository;
import li.naska.bgg.repository.model.BggGeekplayRequestBody;
import li.naska.bgg.repository.model.BggGeekplayResponseBody;
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
@RequestMapping("/api/v2/geekplay")
public class GeekplayResource {

  @Autowired
  private BggGeekplaysRepository geekplayRepository;

  @Autowired
  private AuthenticationService authenticationService;

  @PostMapping(path = "/current", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayResponseBody> updateCurrentGeekplay(@ParameterObject @Validated @RequestBody BggGeekplayRequestBody params) {
    return authenticationService.authentication().flatMap(
        authn -> geekplayRepository.updateGeekplay(authn.buildBggRequestHeader(), params));
  }

}
