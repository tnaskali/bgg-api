package li.naska.bgg.resource.v3;

import com.boardgamegeek.collection.Collection;
import com.boardgamegeek.plays.Plays;
import com.boardgamegeek.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.model.BggGeekplayResponseBody;
import li.naska.bgg.resource.v3.model.CollectionParams;
import li.naska.bgg.resource.v3.model.Play;
import li.naska.bgg.resource.v3.model.PlaysParams;
import li.naska.bgg.resource.v3.model.UserParams;
import li.naska.bgg.service.ItemsService;
import li.naska.bgg.service.PlaysService;
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
  private ItemsService itemsService;

  @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<User> getUser(@ParameterObject @Validated UserParams parameters) {
    return userService.getUser(parameters);
  }

  @GetMapping(value = "/{username}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Plays> getPlays(@ParameterObject @Validated PlaysParams parameters) {
    return playsService.getPlays(parameters);
  }

  @PostMapping(value = "/{username}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayResponseBody> createPlay(
      @NotNull @PathVariable(value = "username") String username,
      @ParameterObject @Validated @RequestBody Play parameters
  ) {
    return playsService.createPlay(username, parameters);
  }

  @PutMapping(value = "/{username}/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayResponseBody> updatePlay(
      @NotNull @PathVariable(value = "username") String username,
      @NotNull @PathVariable(value = "id") Integer id,
      @ParameterObject @Validated @RequestBody Play parameters
  ) {
    return playsService.updatePlay(username, id, parameters);
  }

  @DeleteMapping(value = "/{username}/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<BggGeekplayResponseBody> deletePlay(
      @NotNull @PathVariable(value = "username") String username,
      @NotNull @PathVariable(value = "id") Integer id
  ) {
    return playsService.deletePlay(username, id);
  }

  @GetMapping(value = "/{username}/items", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Collection> getItems(@ParameterObject @Validated CollectionParams parameters) {
    return itemsService.getCollection(parameters);
  }

}
