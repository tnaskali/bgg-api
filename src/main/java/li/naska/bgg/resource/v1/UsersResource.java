package li.naska.bgg.resource.v1;

import com.boardgamegeek.collection.Collection;
import com.boardgamegeek.geekplay.Play;
import com.boardgamegeek.plays.Plays;
import com.boardgamegeek.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.model.BggCollectionParameters;
import li.naska.bgg.repository.model.BggPlaysParameters;
import li.naska.bgg.repository.model.BggUserParameters;
import li.naska.bgg.service.PlaysService;
import li.naska.bgg.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UsersResource {

  @Autowired
  private PlaysService playsService;

  @Autowired
  private UsersService userService;

  @GetMapping(value = "/{username}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public Mono<User> getUser(@ModelAttribute BggUserParameters parameters) {
    return userService.getUser(parameters);
  }

  @GetMapping(value = "/{username}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Plays> getPlays(BggPlaysParameters parameters) {
    return playsService.getPlays(parameters);
  }

  @PostMapping(value = "/{username}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<Map<String, Object>> createPlay(
      @PathVariable(value = "username") String username,
      @RequestBody Play play
  ) {
    return playsService.savePlay(username, null, play);
  }

  @PutMapping(value = "/{username}/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<Map<String, Object>> updatePlay(
      @PathVariable(value = "username") String username,
      @PathVariable(value = "id") Integer id,
      @RequestBody Play play
  ) {
    return playsService.savePlay(username, id, play);
  }

  @DeleteMapping(value = "/{username}/plays/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<String> deletePlay(
      @PathVariable(value = "username") String username,
      @PathVariable(value = "id") Integer id
  ) {
    return playsService.deletePlay(username, id);
  }

  @GetMapping(value = "/{username}/items", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Collection> getItems(BggCollectionParameters parameters) {
    return userService.getCollection(parameters);
  }

}
