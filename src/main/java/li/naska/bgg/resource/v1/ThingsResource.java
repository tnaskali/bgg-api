package li.naska.bgg.resource.v1;


import com.boardgamegeek.enums.HotItemType;
import com.boardgamegeek.enums.ObjectType;
import com.boardgamegeek.forumlist.Forums;
import com.boardgamegeek.hot.HotItems;
import com.boardgamegeek.plays.Plays;
import com.boardgamegeek.search.Results;
import com.boardgamegeek.thing.Things;
import li.naska.bgg.repository.BggHotRepository;
import li.naska.bgg.repository.model.BggForumsParameters;
import li.naska.bgg.repository.model.BggPlaysParameters;
import li.naska.bgg.repository.model.BggSearchParameters;
import li.naska.bgg.repository.model.BggThingsParameters;
import li.naska.bgg.service.ForumsService;
import li.naska.bgg.service.ItemsService;
import li.naska.bgg.service.PlaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/things")
public class ThingsResource {

  @Autowired
  private BggHotRepository bggHotItemsService;

  @Autowired
  private ItemsService itemsService;

  @Autowired
  private PlaysService playsService;

  @Autowired
  private ForumsService forumsService;

  @GetMapping(value = "/hot", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<HotItems> getThings(@RequestParam(value = "type") HotItemType type) {
    return itemsService.getHotItems(type);
  }

  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Results> searchThings(BggSearchParameters parameters) {
    return itemsService.getResults(parameters);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Things> getThingsOnlyParams(BggThingsParameters parameters) {
    return itemsService.getThings(parameters);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Things> getThings(BggThingsParameters parameters) {
    return itemsService.getThings(parameters);
  }

  @GetMapping(value = "/{id}/forums", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Forums> getForums(BggForumsParameters parameters) {
    parameters.setType(ObjectType.thing);
    return forumsService.getForums(parameters);
  }

  @GetMapping(value = "/{id}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Plays> getPlays(BggPlaysParameters parameters) {
    parameters.setType(ObjectType.thing);
    return playsService.getPlays(parameters);
  }

}
