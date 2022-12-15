package li.naska.bgg.resource.v4;

import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggGeeklistsV4Repository;
import li.naska.bgg.repository.model.BggGeeklistReactionsV4QueryParams;
import li.naska.bgg.repository.model.BggGeeklistTipsV4QueryParams;
import li.naska.bgg.repository.model.BggGeeklistsV4QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("GeeklistsV4Resource")
@RequestMapping("/api/v4/geeklists")
public class GeeklistsResource {

  @Autowired
  private BggGeeklistsV4Repository geeklistsRepository;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getGeeklists(@Validated BggGeeklistsV4QueryParams params) {
    return geeklistsRepository.getGeeklists(params);
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getGeeklist(@NotNull @PathVariable Integer id) {
    return geeklistsRepository.getGeeklist(id);
  }

  @GetMapping(path = "/{id}/reactions", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getGeeklistReactions(@NotNull @PathVariable Integer id,
                                           @Validated BggGeeklistReactionsV4QueryParams params) {
    return geeklistsRepository.getGeeklistReactions(id, params);
  }

  @GetMapping(path = "/{id}/tips", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getGeeklistTips(@NotNull @PathVariable Integer id,
                                      @Validated BggGeeklistTipsV4QueryParams params) {
    return geeklistsRepository.getGeeklistTips(id, params);
  }

}
