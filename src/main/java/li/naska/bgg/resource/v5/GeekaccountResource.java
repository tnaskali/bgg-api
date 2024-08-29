package li.naska.bgg.resource.v5;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggGeekaccountV5Repository;
import li.naska.bgg.repository.model.BggGeekaccountToplistV5QueryParams;
import li.naska.bgg.repository.model.BggGeekaccountToplistV5ResponseBody;
import li.naska.bgg.service.AuthenticationService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("GeekaccountV5Resource")
@RequestMapping("/api/v5/geekaccount")
public class GeekaccountResource {

  @Autowired private BggGeekaccountV5Repository geekaccountRepository;

  @Autowired private AuthenticationService authenticationService;

  @GetMapping(path = "/toplist")
  @Operation(
      summary = "Get account toplist information for the current user",
      description = "Get account toplist information for the current user.",
      security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekaccountToplistV5ResponseBody> getGeekaccountToplist(
      @Validated @ParameterObject BggGeekaccountToplistV5QueryParams params) {
    return authenticationService
        .requiredAuthentication()
        .flatMap(
            authn ->
                geekaccountRepository.getGeekaccountToplist(authn.buildBggRequestHeader(), params));
  }
}
