package li.naska.bgg.resource.v1;

import com.boardgamegeek.geeklist.Geeklist;
import li.naska.bgg.repository.model.BggGeeklistParameters;
import li.naska.bgg.service.GeeklistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/geeklists")
public class GeeklistsResource {

  @Autowired
  private GeeklistsService geeklistsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Geeklist> getGeeklist(BggGeeklistParameters parameters) {
    return geeklistsService.getGeeklist(parameters);
  }

}
