package li.naska.bgg.resource.v3;

import com.boardgamegeek.geeklist.Geeklist;
import li.naska.bgg.resource.v3.model.GeeklistParams;
import li.naska.bgg.service.GeeklistsService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v3/geeklists")
public class GeeklistsResource {

  @Autowired
  private GeeklistsService geeklistsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Geeklist> getGeeklist(@ParameterObject @Validated GeeklistParams params) {
    return geeklistsService.getGeeklist(params);
  }

}
