package li.naska.bgg.resource.v5;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggGeekaccountV5Repository;
import li.naska.bgg.repository.model.BggGeekaccountToplistV5QueryParams;
import li.naska.bgg.repository.model.BggGeekaccountToplistV5ResponseBody;
import li.naska.bgg.service.AuthenticationService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("geekaccountV5Resource")
@RequestMapping("/api/v5/geekaccount")
public class GeekaccountResource {

  private final BggGeekaccountV5Repository geekaccountRepository;

  private final AuthenticationService authenticationService;

  public GeekaccountResource(
      BggGeekaccountV5Repository geekaccountRepository,
      AuthenticationService authenticationService) {
    this.geekaccountRepository = geekaccountRepository;
    this.authenticationService = authenticationService;
  }

  @GetMapping(path = "/toplist")
  @Operation(
      summary = "Get account toplist information for the current user",
      description = "Get account toplist information for the current user.",
      security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekaccountToplistV5ResponseBody> getGeekaccountToplist(
      @Validated @ParameterObject BggGeekaccountToplistV5QueryParams params) {
    return authenticationService
        .authenticationCookieHeaderValue()
        .flatMap(cookie -> geekaccountRepository.getGeekaccountToplist(cookie, params));
  }
}
