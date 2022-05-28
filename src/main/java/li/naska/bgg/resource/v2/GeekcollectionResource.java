package li.naska.bgg.resource.v2;

import li.naska.bgg.repository.BggGeekcollectionsRepository;
import li.naska.bgg.repository.model.BggGeekcollectionQueryParams;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v2/geekcollection")
public class GeekcollectionResource {

  @Autowired
  private BggGeekcollectionsRepository geekcollectionRepository;

  @GetMapping(produces = "text/csv")
  public Mono<String> getGeekcollection(@ParameterObject @Validated BggGeekcollectionQueryParams params) {
    return geekcollectionRepository.getGeekcollection(params);
  }

}
