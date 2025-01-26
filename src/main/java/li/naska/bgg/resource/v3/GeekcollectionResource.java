package li.naska.bgg.resource.v3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggGeekcollectionV3Repository;
import li.naska.bgg.repository.model.BggGeekcollectionV3QueryParams;
import li.naska.bgg.service.AuthenticationService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("geekcollectionV3Resource")
@RequestMapping("/api/v3/geekcollection")
public class GeekcollectionResource {

  private final BggGeekcollectionV3Repository geekcollectionRepository;

  private final AuthenticationService authenticationService;

  public GeekcollectionResource(
      BggGeekcollectionV3Repository geekcollectionRepository,
      AuthenticationService authenticationService) {
    this.geekcollectionRepository = geekcollectionRepository;
    this.authenticationService = authenticationService;
  }

  @GetMapping(produces = "text/csv")
  @Operation(
      summary = "Exports a user's collection",
      description = "Exports a user's collection",
      security = @SecurityRequirement(name = "basicAuth"))
  public Mono<String> getGeekcollection(
      @Validated @ParameterObject BggGeekcollectionV3QueryParams params) {
    return authenticationService
        .optionalAuthenticationCookieHeaderValue()
        .flatMap(cookie -> geekcollectionRepository.getGeekcollectionAsCsv(cookie, params));
  }
}
