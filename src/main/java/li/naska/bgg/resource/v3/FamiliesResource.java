package li.naska.bgg.resource.v3;

import com.boardgamegeek.forumlist.Forums;
import li.naska.bgg.resource.v3.model.*;
import li.naska.bgg.service.FamiliesService;
import li.naska.bgg.service.ForumsService;
import li.naska.bgg.service.PlaysService;
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
@RequestMapping("/api/v3/families")
public class FamiliesResource {

  @Autowired
  private FamiliesService familiesService;

  @Autowired
  private PlaysService playsService;

  @Autowired
  private ForumsService forumsService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<Family>> getFamilies(
      @ParameterObject @Validated FamiliesParams parameters) {
    return familiesService.getFamilies(parameters);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Family> getFamily(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated FamilyParams parameters) {
    return familiesService.getFamily(id, parameters);
  }

  @GetMapping(value = "/{id}/forums", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Forums> getForums(
      @NotNull @PathVariable Integer id) {
    return forumsService.getFamilyForums(id);
  }

  @GetMapping(value = "/{id}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Plays> getPlays(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated ItemPlaysParams parameters) {
    return playsService.getFamilyPlays(id, parameters);
  }

}
