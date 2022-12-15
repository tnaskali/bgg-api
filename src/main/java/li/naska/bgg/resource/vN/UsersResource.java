package li.naska.bgg.resource.vN;

import jakarta.validation.constraints.NotNull;
import li.naska.bgg.resource.vN.model.*;
import li.naska.bgg.resource.vN.model.User.Buddy;
import li.naska.bgg.resource.vN.model.User.Ranking.RankedItem;
import li.naska.bgg.service.PlaysService;
import li.naska.bgg.service.ThingsService;
import li.naska.bgg.service.UsersService;
import li.naska.bgg.util.Page;
import li.naska.bgg.util.PagingParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/vN/users")
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
      @NotNull @PathVariable String username) {
    return userService.getUser(username);
  }

  @GetMapping(value = "/{username}/buddies", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Page<Buddy>> getBuddies(
      @NotNull @PathVariable String username,
      @Validated PagingParams pagingParams) {
    return userService.getPagedBuddies(username, pagingParams);
  }

  @GetMapping(value = "/{username}/guilds", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Page<Guild>> getGuilds(
      @NotNull @PathVariable String username,
      @Validated PagingParams pagingParams) {
    return userService.getPagedGuilds(username, pagingParams);
  }

  @GetMapping(value = "/{username}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Page<Play>> getPlays(
      @NotNull @PathVariable String username,
      @Validated UserPlaysParams params,
      @Validated PagingParams pagingParams) {
    return playsService.getPagedUserPlays(username, params, pagingParams);
  }

  @GetMapping(value = "/{username}/things", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Collection> getThings(
      @NotNull @PathVariable String username,
      @Validated CollectionParams params) {
    return thingsService.getThings(username, params);
  }

  @GetMapping(value = "/{username}/hot", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<RankedItem>> getHotItems(
      @NotNull @PathVariable String username,
      @Validated UserRankedItemsParams params) {
    return userService.getHotItems(username, params);
  }

  @GetMapping(value = "/{username}/top", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<RankedItem>> getTopItems(
      @NotNull @PathVariable String username,
      @Validated UserRankedItemsParams params) {
    return userService.getTopItems(username, params);
  }

}
