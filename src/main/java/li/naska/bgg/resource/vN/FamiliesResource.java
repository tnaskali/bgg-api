package li.naska.bgg.resource.vN;

import jakarta.validation.constraints.NotNull;
import li.naska.bgg.resource.vN.model.*;
import li.naska.bgg.service.FamiliesService;
import li.naska.bgg.service.ForumsService;
import li.naska.bgg.service.PlaysService;
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
@RequestMapping("/api/vN/families")
public class FamiliesResource {

  @Autowired
  private FamiliesService familiesService;

  @Autowired
  private PlaysService playsService;

  @Autowired
  private ForumsService forumsService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<Family>> getFamilies(
      @Validated FamiliesParams params) {
    return familiesService.getFamilies(params);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Family> getFamily(
      @NotNull @PathVariable Integer id) {
    return familiesService.getFamily(id);
  }

  @GetMapping(value = "/{id}/forums", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<Forum>> getForums(
      @NotNull @PathVariable Integer id) {
    return forumsService.getFamilyForums(id);
  }

  @GetMapping(value = "/{id}/plays", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Page<Play>> getPlays(
      @NotNull @PathVariable Integer id,
      @Validated ItemPlaysParams params,
      @Validated PagingParams pagingParams) {
    return playsService.getPagedFamilyPlays(id, params, pagingParams);
  }

}
