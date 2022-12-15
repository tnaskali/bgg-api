package li.naska.bgg.resource.vN;

import jakarta.validation.constraints.NotNull;
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
  private UsersService userService;

  @Autowired
  private ThingsService thingsService;

  @Autowired
  private AuthenticationService authenticationService;

  @GetMapping(value = "/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Page<Play>> getPlays(
      @Validated UserPlaysParams params,
      @Validated PagingParams pagingParams) {
    return authenticationService.authentication()
        .flatMap(authn -> playsService.getPagedUserPlays(authn.getPrincipal(), params, pagingParams));
  }

  @PostMapping(value = "/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<BggGeekplayV3ResponseBody> createPlay(
      @Validated @RequestBody Play requestBody) {
    return authenticationService.authentication()
        .flatMap(authn -> playsService.createPrivatePlay(authn.buildBggRequestHeader(), requestBody));
  }

  @PutMapping(value = "/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<BggGeekplayV3ResponseBody> updatePlay(
      @NotNull @PathVariable Integer id,
      @Validated @RequestBody Play requestBody) {
    return authenticationService.authentication()
        .flatMap(authn -> playsService.updatePrivatePlay(id, authn.buildBggRequestHeader(), requestBody));
  }

  @DeleteMapping(value = "/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<BggGeekplayV3ResponseBody> deletePlay(
      @NotNull @PathVariable Integer id) {
    return authenticationService.authentication()
        .flatMap(authn -> playsService.deletePrivatePlay(id, authn.buildBggRequestHeader()));
  }

  @GetMapping(value = "/things", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Collection> getThings(
      @Validated CollectionParams queryParams) {
    return authenticationService.authentication()
        .flatMap(authn -> thingsService.getPrivateThings(authn.getPrincipal(), authn.buildBggRequestHeader(), queryParams));
  }

}
