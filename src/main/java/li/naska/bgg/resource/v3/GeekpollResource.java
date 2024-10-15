package li.naska.bgg.resource.v3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggGeekpollV3Repository;
import li.naska.bgg.repository.model.BggGeekpollV3QueryParams;
import li.naska.bgg.repository.model.BggGeekpollV3ResponseBody;
import li.naska.bgg.security.BggAuthenticationToken;
import li.naska.bgg.service.AuthenticationService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("GeekpollV3Resource")
@RequestMapping("/api/v3/geekpoll")
public class GeekpollResource {

  private final BggGeekpollV3Repository geekpollRepository;

  private final AuthenticationService authenticationService;

  public GeekpollResource(
      BggGeekpollV3Repository geekpollRepository, AuthenticationService authenticationService) {
    this.geekpollRepository = geekpollRepository;
    this.authenticationService = authenticationService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get poll",
      description = "Get poll information by id.",
      security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekpollV3ResponseBody> getGeekpoll(
      @Validated @ParameterObject BggGeekpollV3QueryParams params) {
    return authenticationService
        .optionalAuthentication()
        .flatMap(authn -> geekpollRepository.getGeekpoll(
            authn.map(BggAuthenticationToken::buildBggRequestHeader), params));
  }
}
