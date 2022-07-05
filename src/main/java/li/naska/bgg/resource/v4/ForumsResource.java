package li.naska.bgg.resource.v4;

import li.naska.bgg.repository.BggForumsV4Repository;
import li.naska.bgg.repository.model.BggForumsV4QueryParams;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("ForumsV4Resource")
@RequestMapping("/api/v4/forums")
public class ForumsResource {

  @Autowired
  private BggForumsV4Repository forumsRepository;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getForums(@ParameterObject @Validated BggForumsV4QueryParams params) {
    return forumsRepository.getForums(params);
  }

}
