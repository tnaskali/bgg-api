package li.naska.bgg.resource.v1;

import com.boardgamegeek.enums.FamilyType;
import com.boardgamegeek.enums.ObjectSubtype;
import com.boardgamegeek.enums.ObjectType;
import com.boardgamegeek.family.Families;
import com.boardgamegeek.forumlist.Forums;
import com.boardgamegeek.plays.Plays;
import li.naska.bgg.repository.BggFamiliesRepository;
import li.naska.bgg.repository.BggForumListsRepository;
import li.naska.bgg.repository.BggPlaysRepository;
import li.naska.bgg.repository.model.BggFamiliesParameters;
import li.naska.bgg.repository.model.BggForumsParameters;
import li.naska.bgg.repository.model.BggItemPlaysParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/families")
public class FamiliesResource {

  @Autowired
  private BggFamiliesRepository bggFamiliesService;

  @Autowired
  private BggPlaysRepository bggPlaysService;

  @Autowired
  private BggForumListsRepository bggForumListsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Families> getFamilies(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "types", required = false) List<FamilyType> types
  ) {
    BggFamiliesParameters parameters = new BggFamiliesParameters(id);
    parameters.setTypes(types);
    return bggFamiliesService.getFamily(parameters);
  }

  @GetMapping(value = "/{id}/forums", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Forums> getForums(
      @PathVariable(value = "id") Integer id
  ) {
    BggForumsParameters parameters = new BggForumsParameters(id, ObjectType.FAMILY);
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
    BggItemPlaysParameters parameters = new BggItemPlaysParameters(id, ObjectType.FAMILY);
    parameters.setUsername(username);
    parameters.setMindate(mindate);
    parameters.setMaxdate(maxdate);
    parameters.setSubtype(subtype);
    parameters.setPage(page);
    return bggPlaysService.getPlays(parameters);
  }

}
