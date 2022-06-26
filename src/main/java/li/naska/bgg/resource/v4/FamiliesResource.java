package li.naska.bgg.resource.v4;

import li.naska.bgg.repository.BggFamiliesV4Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("FamiliesV4Resource")
@RequestMapping("/api/v4/families")
public class FamiliesResource {

  @Autowired
  private BggFamiliesV4Repository familiesRepository;

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getFamily(@PathVariable Integer id) {
    return familiesRepository.getFamily(id);
  }

}
