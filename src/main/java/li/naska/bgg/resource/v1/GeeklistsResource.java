package li.naska.bgg.resource.v1;

import com.boardgamegeek.geeklist.Geeklist;
import li.naska.bgg.repository.BggGeeklistsRepository;
import li.naska.bgg.repository.model.BggGeeklistsParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/geeklists")
public class GeeklistsResource {

  @Autowired
  private BggGeeklistsRepository bggGeeklistsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Geeklist> getGeeklist(
      @PathVariable(value = "id") Integer id,
      @RequestParam(value = "comments", required = false) Boolean comments
  ) {
    BggGeeklistsParameters parameters = new BggGeeklistsParameters(id);
    parameters.setComments(comments);
    return bggGeeklistsService.getGeeklist(parameters);
  }

}
