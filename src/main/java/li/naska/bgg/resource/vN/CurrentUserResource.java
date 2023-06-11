package li.naska.bgg.resource.vN;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.model.BggGeekplayV3ResponseBody;
import li.naska.bgg.resource.vN.model.Collection;
import li.naska.bgg.resource.vN.model.CollectionParams;
import li.naska.bgg.resource.vN.model.Play;
import li.naska.bgg.resource.vN.model.UserPlaysParams;
import li.naska.bgg.service.AuthenticationService;
import li.naska.bgg.service.PlaysService;
import li.naska.bgg.service.ThingsService;
import li.naska.bgg.util.Page;
import li.naska.bgg.util.PagingParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/vN/users/current")
@Validated
public class CurrentUserResource {

  @Autowired
  private PlaysService playsService;

  @Autowired
  private ThingsService thingsService;

  @Autowired
  private AuthenticationService authenticationService;

  @GetMapping(value = "/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<Page<Play>> getPlays(@Validated UserPlaysParams params,
                                   @Validated PagingParams pagingParams) {
    return authenticationService.requiredAuthentication()
        .flatMap(authn -> playsService.getPagedUserPlays(authn.getPrincipal(), params, pagingParams));
  }

  @PostMapping(value = "/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayV3ResponseBody> createPlay(@Validated @RequestBody Play requestBody) {
    return authenticationService.requiredAuthentication()
        .flatMap(authn -> playsService.createPrivatePlay(authn.buildBggRequestHeader(), requestBody));
  }

  @PutMapping(value = "/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayV3ResponseBody> updatePlay(@NotNull @PathVariable Integer id,
                                                    @Validated @RequestBody Play requestBody) {
    return authenticationService.requiredAuthentication()
        .flatMap(authn -> playsService.updatePrivatePlay(id, authn.buildBggRequestHeader(), requestBody));
  }

  @DeleteMapping(value = "/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayV3ResponseBody> deletePlay(@NotNull @PathVariable Integer id) {
    return authenticationService.requiredAuthentication()
        .flatMap(authn -> playsService.deletePrivatePlay(id, authn.buildBggRequestHeader()));
  }

  @GetMapping(value = "/things", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<Collection> getThings(@Validated CollectionParams queryParams) {
    return authenticationService.requiredAuthentication()
        .flatMap(authn -> thingsService.getPrivateThings(authn.getPrincipal(), authn.buildBggRequestHeader(), queryParams));
  }

}
