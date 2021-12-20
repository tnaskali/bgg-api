package li.naska.bgg.resource.v3;


import com.boardgamegeek.forumlist.Forums;
import com.boardgamegeek.hot.HotItems;
import com.boardgamegeek.plays.Plays;
import com.boardgamegeek.search.Results;
import li.naska.bgg.repository.BggHotItemsRepository;
import li.naska.bgg.resource.v3.model.*;
import li.naska.bgg.service.ForumsService;
import li.naska.bgg.service.ItemsService;
import li.naska.bgg.service.PlaysService;
import li.naska.bgg.service.ThingsService;
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
import java.util.List;

@RestController
@RequestMapping("/api/v3/things")
@Validated
public class ThingsResource {

  @Autowired
  private BggHotItemsRepository bggHotItemsService;

  @Autowired
  private ItemsService itemsService;

  @Autowired
  private ThingsService thingsService;

  @Autowired
  private PlaysService playsService;

  @Autowired
  private ForumsService forumsService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<Thing>> getThings(
      @ParameterObject @Validated ThingsParams parameters) {
    return thingsService.getThings(parameters);
  }

  @GetMapping(value = "/hot", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<HotItems> getHotThings(
      @ParameterObject @Validated HotItemsParams parameters) {
    return itemsService.getHotItems(parameters);
  }

  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Results> searchThings(
      @ParameterObject @Validated SearchParams parameters) {
    return itemsService.searchItems(parameters);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Thing> getThing(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated ThingParams parameters) {
    return thingsService.getThing(id, parameters);
  }

  @GetMapping(value = "/{id}/forums", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Forums> getForums(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated ForumsParams parameters) {
    return forumsService.getForums(id, parameters);
  }

  @GetMapping(value = "/{id}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Plays> getPlays(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated ItemPlaysParams parameters) {
    return playsService.getThingPlays(id, parameters);
  }

}
