package li.naska.bgg.resource.v4;

import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggListitemsV4Repository;
import li.naska.bgg.repository.model.BggListitemReactionsV4QueryParams;
import li.naska.bgg.repository.model.BggListitemTipsV4QueryParams;
import li.naska.bgg.repository.model.BggListitemsV4QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("ListitemsV4Resource")
@RequestMapping("/api/v4/listitems")
public class ListitemsResource {

  @Autowired
  private BggListitemsV4Repository listitemsRepository;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getListitems(@Validated BggListitemsV4QueryParams params) {
    return listitemsRepository.getListitems(params);
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getListitems(@NotNull @PathVariable Integer id) {
    return listitemsRepository.getListitem(id);
  }

  @GetMapping(path = "/{id}/reactions", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getListitemsReactions(@NotNull @PathVariable Integer id,
                                            @Validated BggListitemReactionsV4QueryParams params) {
    return listitemsRepository.getListitemReactions(id, params);
  }

  @GetMapping(path = "/{id}/tips", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getListitemsTips(@NotNull @PathVariable Integer id,
                                       @Validated BggListitemTipsV4QueryParams params) {
    return listitemsRepository.getListitemTips(id, params);
  }

}
