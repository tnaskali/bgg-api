package li.naska.bgg.resource.v1;


import com.boardgamegeek.enums.HotItemType;
import com.boardgamegeek.enums.ObjectSubtype;
import com.boardgamegeek.enums.ObjectType;
import com.boardgamegeek.forumlist.Forums;
import com.boardgamegeek.hot.HotItems;
import com.boardgamegeek.plays.Plays;
import com.boardgamegeek.search.Results;
import com.boardgamegeek.thing.Things;
import li.naska.bgg.repository.*;
import li.naska.bgg.repository.model.BggForumsParameters;
import li.naska.bgg.repository.model.BggItemPlaysParameters;
import li.naska.bgg.repository.model.BggSearchParameters;
import li.naska.bgg.repository.model.BggThingsParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/things")
public class ThingsResource {

  @Autowired
  private BggHotItemsRepository bggHotItemsService;

  @Autowired
  private BggSearchRepository bggSearchService;

  @Autowired
  private BggThingsRepository bggThingsService;

  @Autowired
  private BggPlaysRepository bggPlaysService;

  @Autowired
  private BggForumListsRepository bggForumListsService;

  @GetMapping(value = "/hot", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<HotItems> getThings(
      @RequestParam(value = "type") HotItemType type
  ) {
    return bggHotItemsService.getItems(type);
  }

  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Results> searchThings(
      @RequestParam(value = "query") String query,
      @RequestParam(value = "type", required = false) List<ObjectSubtype> types,
      @RequestParam(value = "exact", required = false) Boolean exact
  ) {
    BggSearchParameters parameters = new BggSearchParameters(query);
    parameters.setTypes(types);
    parameters.setExact(exact);
    return bggSearchService.getItems(parameters);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Things> getThings(
      @RequestParam(value = "id") List<Integer> ids,
      @RequestParam(value = "type", required = false) List<ObjectSubtype> types,
      @RequestParam(value = "versions", required = false) Boolean versions,
      @RequestParam(value = "videos", required = false) Boolean videos,
      @RequestParam(value = "stats", required = false) Boolean stats,
      @RequestParam(value = "marketplace", required = false) Boolean marketplace,
      @RequestParam(value = "comments", required = false) Boolean comments,
      @RequestParam(value = "ratingcomments", required = false) Boolean ratingcomments,
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "pagesize", required = false) Integer pagesize
  ) {
    BggThingsParameters parameters = new BggThingsParameters(ids);
    parameters.setType(types);
    parameters.setVersions(versions);
    parameters.setVideos(videos);
    parameters.setStats(stats);
    parameters.setMarketplace(marketplace);
    parameters.setComments(comments);
    parameters.setRatingcomments(ratingcomments);
    parameters.setPage(page);
    parameters.setPagesize(pagesize);
    return bggThingsService.getThings(parameters);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Things> getThing(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "versions", required = false) Boolean versions,
      @RequestParam(value = "videos", required = false) Boolean videos,
      @RequestParam(value = "stats", required = false) Boolean stats,
      @RequestParam(value = "marketplace", required = false) Boolean marketplace,
      @RequestParam(value = "comments", required = false) Boolean comments,
      @RequestParam(value = "ratingcomments", required = false) Boolean ratingcomments
  ) {
    BggThingsParameters parameters = new BggThingsParameters(Collections.singletonList(id));
    parameters.setVersions(versions);
    parameters.setVideos(videos);
    parameters.setStats(stats);
    parameters.setMarketplace(marketplace);
    parameters.setComments(comments);
    parameters.setRatingcomments(ratingcomments);
    return bggThingsService.getThings(parameters);
  }

  @GetMapping(value = "/{id}/forums", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Forums> getForums(
      @PathVariable(value = "id") Integer id
  ) {
    BggForumsParameters parameters = new BggForumsParameters(id, ObjectType.THING);
    return bggForumListsService.getForums(parameters);
  }

  @GetMapping(value = "/{id}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Plays> getPlays(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "username", required = false) String username,
      @RequestParam(value = "mindate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate mindate,
      @RequestParam(value = "maxdate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate maxdate,
      @RequestParam(value = "subtype", required = false) ObjectSubtype subtype,
      @RequestParam(value = "page", required = false) Integer page
  ) {
    BggItemPlaysParameters parameters = new BggItemPlaysParameters(id, ObjectType.THING);
    parameters.setUsername(username);
    parameters.setMindate(mindate);
    parameters.setMaxdate(maxdate);
    parameters.setSubtype(subtype);
    parameters.setPage(page);
    return bggPlaysService.getPlays(parameters);
  }

}
