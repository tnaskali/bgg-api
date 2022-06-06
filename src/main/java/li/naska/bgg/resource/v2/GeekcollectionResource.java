package li.naska.bgg.resource.v2;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggGeekcollectionsRepository;
import li.naska.bgg.repository.model.BggGeekcollectionQueryParams;
import li.naska.bgg.service.AuthenticationService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v2/geekcollection")
public class GeekcollectionResource {

  @Autowired
  private BggGeekcollectionsRepository geekcollectionRepository;

  @Autowired
  private AuthenticationService authenticationService;

  @GetMapping(produces = "text/csv")
  public Mono<String> getGeekcollection(@ParameterObject @Validated BggGeekcollectionQueryParams params) {
    return geekcollectionRepository.getGeekcollection(null, params);
  }

  @GetMapping(path = "/current", produces = "text/csv")
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<String> getCurrentGeekcollection(@ParameterObject @Validated BggGeekcollectionQueryParams params) {
    return authenticationService.authentication().flatMap(
        authn -> geekcollectionRepository.getGeekcollection(authn.buildBggRequestHeader(), params));
  }

}
