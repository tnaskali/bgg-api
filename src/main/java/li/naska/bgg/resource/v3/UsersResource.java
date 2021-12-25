package li.naska.bgg.resource.v3;

import li.naska.bgg.resource.v3.model.*;
import li.naska.bgg.service.PlaysService;
import li.naska.bgg.service.ThingsService;
import li.naska.bgg.service.UsersService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

  @GetMapping(value = "/{username}/things", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Collection> getThings(
      @NotNull @PathVariable String username,
      @ParameterObject @Validated CollectionParams queryParameters) {
    return thingsService.getThings(username, queryParameters);
  }

}
