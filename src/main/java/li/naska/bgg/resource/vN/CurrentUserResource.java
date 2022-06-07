package li.naska.bgg.resource.vN;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.model.BggGeekplayV3ResponseBody;
import li.naska.bgg.resource.vN.model.Collection;
import li.naska.bgg.resource.vN.model.CollectionParams;
import li.naska.bgg.resource.vN.model.Play;
import li.naska.bgg.resource.vN.model.UserPlaysParams;
import li.naska.bgg.service.AuthenticationService;
import li.naska.bgg.service.PlaysService;
import li.naska.bgg.service.ThingsService;
import li.naska.bgg.service.UsersService;
import li.naska.bgg.util.Page;
import li.naska.bgg.util.PagingParams;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/vN/users/current")
@Validated
public class CurrentUserResource {

  @Autowired
  private PlaysService playsService;

  @Autowired
  private UsersService userService;

  @Autowired
  private ThingsService thingsService;

  @Autowired
  private AuthenticationService authenticationService;

  @GetMapping(value = "/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<Page<Play>> getPlays(
      @ParameterObject @Validated UserPlaysParams params,
      @ParameterObject @Validated PagingParams pagingParams) {
    return authenticationService.authentication()
        .flatMap(authn -> playsService.getPagedUserPlays(authn.getPrincipal(), params, pagingParams));
  }

  @PostMapping(value = "/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayV3ResponseBody> createPlay(
      @ParameterObject @Validated @RequestBody Play requestBody) {
    return authenticationService.authentication()
        .flatMap(authn -> playsService.createPrivatePlay(authn.buildBggRequestHeader(), requestBody));
  }

  @PutMapping(value = "/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayV3ResponseBody> updatePlay(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated @RequestBody Play requestBody) {
    return authenticationService.authentication()
        .flatMap(authn -> playsService.updatePrivatePlay(id, authn.buildBggRequestHeader(), requestBody));
  }

  @DeleteMapping(value = "/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayV3ResponseBody> deletePlay(
      @NotNull @PathVariable Integer id) {
    return authenticationService.authentication()
        .flatMap(authn -> playsService.deletePrivatePlay(id, authn.buildBggRequestHeader()));
  }

  @GetMapping(value = "/things", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<Collection> getThings(
      @ParameterObject @Validated CollectionParams queryParams) {
    return authenticationService.authentication()
        .flatMap(authn -> thingsService.getPrivateThings(authn.getPrincipal(), authn.buildBggRequestHeader(), queryParams));
  }

}
