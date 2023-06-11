package li.naska.bgg.resource.v3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggGeekaccountV3Repository;
import li.naska.bgg.repository.model.BggGeekaccountV3RequestBody;
import li.naska.bgg.repository.model.BggGeekaccountV3ResponseBody;
import li.naska.bgg.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("GeekaccountV3Resource")
@RequestMapping("/api/v3/geekaccount")
public class GeekaccountResource {

  @Autowired
  private BggGeekaccountV3Repository geekaccountRepository;

  @Autowired
  private AuthenticationService authenticationService;

  @PostMapping(path = "/current", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekaccountV3ResponseBody> updateCurrentGeekaccount(@Validated @RequestBody BggGeekaccountV3RequestBody params) {
    return authenticationService.requiredAuthentication().flatMap(
        authn -> geekaccountRepository.updateGeekaccount(authn.buildBggRequestHeader(), params));
  }

}
