package li.naska.bgg.resource.v4;

import li.naska.bgg.repository.BggGeekitemV4Repository;
import li.naska.bgg.repository.BggGeekitemsV4Repository;
import li.naska.bgg.repository.model.BggGeekitemLinkeditemsV4QueryParams;
import li.naska.bgg.repository.model.BggGeekitemsV4QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("GeekitemsV4Resource")
@RequestMapping("/api/v4/geekitems")
public class GeekitemsResource {

  @Autowired
  private BggGeekitemsV4Repository geekitemsRepository;

  @Autowired
  private BggGeekitemV4Repository geekitemRepository;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getGeekitem(@Validated BggGeekitemsV4QueryParams params) {
    return geekitemsRepository.getGeekitem(params);
  }

  @GetMapping(path = "/linkeditems", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getGeekitemLinkeditems(@Validated BggGeekitemLinkeditemsV4QueryParams params) {
    return geekitemRepository.getGeekitemLinkeditems(params);
  }

}
