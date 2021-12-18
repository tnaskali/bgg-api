package li.naska.bgg.resource.v3;

import com.boardgamegeek.enums.ItemType;
import com.boardgamegeek.family.Families;
import com.boardgamegeek.forumlist.Forums;
import com.boardgamegeek.plays.Plays;
import li.naska.bgg.resource.v3.model.FamiliesParams;
import li.naska.bgg.resource.v3.model.ForumsParams;
import li.naska.bgg.resource.v3.model.PlaysParams;
import li.naska.bgg.service.FamiliesService;
import li.naska.bgg.service.ForumsService;
import li.naska.bgg.service.PlaysService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v3/families")
public class FamiliesResource {

  @Autowired
  private FamiliesService familiesService;

  @Autowired
  private PlaysService playsService;

  @Autowired
  private ForumsService forumsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Families> getFamilies(@ParameterObject @Validated FamiliesParams parameters) {
    return familiesService.getFamilies(parameters);
  }

  @GetMapping(value = "/{id}/forums", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Forums> getForums(@ParameterObject @Validated ForumsParams parameters) {
    parameters.setType(ItemType.family);
    return forumsService.getForums(parameters);
  }

  @GetMapping(value = "/{id}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Plays> getPlays(@ParameterObject @Validated PlaysParams parameters) {
    parameters.setType(ItemType.family);
    return playsService.getPlays(parameters);
  }

}
