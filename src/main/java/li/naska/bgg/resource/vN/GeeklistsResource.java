package li.naska.bgg.resource.vN;

import jakarta.validation.constraints.NotNull;
import li.naska.bgg.resource.vN.model.Geeklist;
import li.naska.bgg.resource.vN.model.GeeklistParams;
import li.naska.bgg.service.GeeklistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/vN/geeklists")
public class GeeklistsResource {

  @Autowired
  private GeeklistsService geeklistsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Geeklist> getGeeklist(
      @NotNull @PathVariable Integer id,
      @Validated GeeklistParams params) {
    return geeklistsService.getGeeklist(id, params);
  }

}
