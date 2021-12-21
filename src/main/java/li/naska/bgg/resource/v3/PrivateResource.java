package li.naska.bgg.resource.v3;

import com.boardgamegeek.plays.Plays;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.model.BggGeekplayResponseBody;
import li.naska.bgg.resource.v3.model.Collection;
import li.naska.bgg.resource.v3.model.CollectionParams;
import li.naska.bgg.resource.v3.model.Play;
import li.naska.bgg.resource.v3.model.UserPlaysParams;
import li.naska.bgg.security.BggAuthenticationToken;
import li.naska.bgg.service.PlaysService;
import li.naska.bgg.service.ThingsService;
import li.naska.bgg.service.UsersService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v3/private")
@Validated
public class PrivateResource {

  @Autowired
  private PlaysService playsService;

  @Autowired
  private UsersService userService;

  @Autowired
  private ThingsService thingsService;

  private Mono<BggAuthenticationToken> authentication() {
    return ReactiveSecurityContextHolder.getContext().map(
        context -> (BggAuthenticationToken) context.getAuthentication()
    );
  }

  @GetMapping(value = "/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<Plays> getPlays(
      @ParameterObject @Validated UserPlaysParams parameters) {
    return authentication()
        .flatMap(authn -> playsService.getUserPlays(authn.getPrincipal(), parameters));
  }

  @PostMapping(value = "/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayResponseBody> createPlay(
      @ParameterObject @Validated @RequestBody Play requestBody) {
    return authentication()
        .flatMap(authn -> playsService.createPrivatePlay(authn.buildBggRequestHeader(), requestBody));
  }

  @PutMapping(value = "/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayResponseBody> updatePlay(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated @RequestBody Play requestBody) {
    return authentication()
        .flatMap(authn -> playsService.updatePrivatePlay(id, authn.buildBggRequestHeader(), requestBody));
  }

  @DeleteMapping(value = "/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayResponseBody> deletePlay(
      @NotNull @PathVariable Integer id) {
    return authentication()
        .flatMap(authn -> playsService.deletePrivatePlay(id, authn.buildBggRequestHeader()));
  }

  @GetMapping(value = "/things", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<Collection> getThings(
      @ParameterObject @Validated CollectionParams queryParameters) {
    return authentication()
        .flatMap(authn -> thingsService.getPrivateThings(authn.getPrincipal(), authn.buildBggRequestHeader(), queryParameters));
  }

}
