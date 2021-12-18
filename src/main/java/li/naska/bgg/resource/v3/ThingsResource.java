package li.naska.bgg.resource.v3;


import com.boardgamegeek.enums.HotItemType;
import com.boardgamegeek.enums.ItemType;
import com.boardgamegeek.forumlist.Forums;
import com.boardgamegeek.hot.HotItems;
import com.boardgamegeek.plays.Plays;
import com.boardgamegeek.search.Results;
import com.boardgamegeek.thing.Things;
import li.naska.bgg.repository.BggHotItemsRepository;
import li.naska.bgg.resource.v3.model.ForumsParams;
import li.naska.bgg.resource.v3.model.PlaysParams;
import li.naska.bgg.resource.v3.model.ResultsParams;
import li.naska.bgg.resource.v3.model.ThingsParams;
import li.naska.bgg.service.ForumsService;
import li.naska.bgg.service.ItemsService;
import li.naska.bgg.service.PlaysService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v3/things")
@Validated
public class ThingsResource {

  @Autowired
  private BggHotItemsRepository bggHotItemsService;

  @Autowired
  private ItemsService itemsService;

  @Autowired
  private PlaysService playsService;

  @Autowired
  private ForumsService forumsService;

  @GetMapping(value = "/hot", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<HotItems> getThings(@NotNull @RequestParam(value = "type") HotItemType type) {
    return itemsService.getHotItems(type);
  }

  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Results> searchThings(@ParameterObject @Validated ResultsParams parameters) {
    return itemsService.getResults(parameters);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Things> getThingsOnlyParams(@ParameterObject @Validated ThingsParams parameters) {
    return itemsService.getThings(parameters);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Things> getThings(@ParameterObject @Validated ThingsParams parameters) {
    return itemsService.getThings(parameters);
  }

  @GetMapping(value = "/{id}/forums", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Forums> getForums(@ParameterObject @Validated ForumsParams parameters) {
    parameters.setType(ItemType.thing);
    return forumsService.getForums(parameters);
  }

  @GetMapping(value = "/{id}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Plays> getPlays(@ParameterObject @Validated PlaysParams parameters) {
    parameters.setType(ItemType.thing);
    return playsService.getPlays(parameters);
  }

}
