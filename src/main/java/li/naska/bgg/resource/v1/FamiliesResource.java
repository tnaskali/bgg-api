package li.naska.bgg.resource.v1;

import com.boardgamegeek.enums.ObjectType;
import com.boardgamegeek.family.Families;
import com.boardgamegeek.forumlist.Forums;
import com.boardgamegeek.plays.Plays;
import li.naska.bgg.repository.model.BggFamilyParameters;
import li.naska.bgg.repository.model.BggForumsParameters;
import li.naska.bgg.repository.model.BggPlaysParameters;
import li.naska.bgg.service.FamiliesService;
import li.naska.bgg.service.ForumsService;
import li.naska.bgg.service.PlaysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/families")
public class FamiliesResource {

  @Autowired
  private FamiliesService familiesService;

  @Autowired
  private PlaysService playsService;

  @Autowired
  private ForumsService forumsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Families> getFamilies(BggFamilyParameters parameters) {
    return familiesService.getFamilies(parameters);
  }

  @GetMapping(value = "/{id}/forums", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Forums> getForums(BggForumsParameters parameters) {
    parameters.setType(ObjectType.family);
    return forumsService.getForums(parameters);
  }

  @GetMapping(value = "/{id}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Plays> getPlays(BggPlaysParameters parameters) {
    parameters.setType(ObjectType.family);
    return playsService.getPlays(parameters);
  }

}
