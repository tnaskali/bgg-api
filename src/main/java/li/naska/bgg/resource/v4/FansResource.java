package li.naska.bgg.resource.v4;

import li.naska.bgg.repository.BggFansV4Repository;
import li.naska.bgg.repository.model.BggFansV4QueryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("FansV4Resource")
@RequestMapping("/api/v4/fans")
public class FansResource {

  @Autowired
  private BggFansV4Repository fansRepository;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getFans(@Validated BggFansV4QueryParams params) {
    return fansRepository.getFans(params);
  }

}
