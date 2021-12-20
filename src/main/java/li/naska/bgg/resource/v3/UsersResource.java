package li.naska.bgg.resource.v3;

import com.boardgamegeek.collection.Collection;
import com.boardgamegeek.plays.Plays;
import com.boardgamegeek.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.model.BggGeekplayResponseBody;
import li.naska.bgg.resource.v3.model.CollectionParams;
import li.naska.bgg.resource.v3.model.Play;
import li.naska.bgg.resource.v3.model.UserParams;
import li.naska.bgg.resource.v3.model.UserPlaysParams;
import li.naska.bgg.service.PlaysService;
import li.naska.bgg.service.ThingsService;
import li.naska.bgg.service.UsersService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v3/users")
@Validated
public class UsersResource {

  @Autowired
  private PlaysService playsService;

  @Autowired
  private UsersService userService;

  @Autowired
  private ThingsService thingsService;

  @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<User> getUser(
      @NotNull @PathVariable String username,
      @ParameterObject @Validated UserParams parameters) {
    return userService.getUser(username, parameters);
  }

  @GetMapping(value = "/{username}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Plays> getPlays(
      @NotNull @PathVariable String username,
      @ParameterObject @Validated UserPlaysParams parameters) {
    return playsService.getUserPlays(username, parameters);
  }

  @PostMapping(value = "/{username}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayResponseBody> createPlay(
      @NotNull @PathVariable String username,
      @ParameterObject @Validated @RequestBody Play requestBody) {
    return playsService.createPlay(username, requestBody);
  }

  @PutMapping(value = "/{username}/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayResponseBody> updatePlay(
      @NotNull @PathVariable String username,
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated @RequestBody Play requestBody) {
    return playsService.updatePlay(username, id, requestBody);
  }

  @DeleteMapping(value = "/{username}/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayResponseBody> deletePlay(
      @NotNull @PathVariable String username,
      @NotNull @PathVariable Integer id) {
    return playsService.deletePlay(username, id);
  }

  @GetMapping(value = "/{username}/things", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Collection> getThings(
      @NotNull @PathVariable String username,
      @ParameterObject @Validated CollectionParams queryParameters) {
    return thingsService.getThings(username, queryParameters);
  }

}
