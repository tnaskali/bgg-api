package li.naska.bgg.resource.v3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggGeekaccountV3Repository;
import li.naska.bgg.repository.model.*;
import li.naska.bgg.service.AuthenticationService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController("GeekaccountV3Resource")
@RequestMapping("/api/v3/geekaccount")
public class GeekaccountResource {

  @Autowired
  private BggGeekaccountV3Repository geekaccountRepository;

  @Autowired
  private AuthenticationService authenticationService;

  @GetMapping(path = "/contact", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get account information for the current user",
      description = "Get account information for the current user",
      security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekaccountContactV3ResponseBody> getGeekaccountContact(@Validated @ParameterObject BggGeekaccountContactV3QueryParams params) {
    return authenticationService.requiredAuthentication().flatMap(
        authn -> geekaccountRepository.getGeekaccountContact(authn.buildBggRequestHeader(), params));
  }

  @PostMapping(path = "/contact", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Update account contact information for the current user",
      description = "Update account contact information for of the current user",
      security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekaccountContactV3ResponseBody> updateGeekaccountContact(@Validated @RequestBody BggGeekaccountContactV3RequestBody body) {
    return authenticationService.requiredAuthentication().flatMap(
        authn -> geekaccountRepository.updateGeekaccountContact(authn.buildBggRequestHeader(), body));
  }

  @PostMapping(path = "/toplist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Update account toplist information for the current user",
      description = "Update account toplist information for of the current user",
      security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekaccountToplistV3ResponseBody> updateGeekaccountToplist(@Validated @RequestBody BggGeekaccountToplistV3RequestBody body) {
    return authenticationService.requiredAuthentication().flatMap(
        authn -> geekaccountRepository.updateGeekaccountToplist(authn.buildBggRequestHeader(), body));
  }

}
