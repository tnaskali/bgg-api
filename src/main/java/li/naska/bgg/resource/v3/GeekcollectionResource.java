package li.naska.bgg.resource.v3;

import li.naska.bgg.repository.BggGeekcollectionV3Repository;
import li.naska.bgg.repository.model.BggGeekcollectionV3QueryParams;
import li.naska.bgg.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("GeekcollectionV3Resource")
@RequestMapping("/api/v3/geekcollection")
public class GeekcollectionResource {

  @Autowired
  private BggGeekcollectionV3Repository geekcollectionRepository;

  @Autowired
  private AuthenticationService authenticationService;

  @GetMapping(produces = "text/csv")
  public Mono<String> getGeekcollection(@Validated BggGeekcollectionV3QueryParams params) {
    return geekcollectionRepository.getGeekcollection(null, params);
  }

  @GetMapping(path = "/current", produces = "text/csv")
  public Mono<String> getCurrentGeekcollection(@Validated BggGeekcollectionV3QueryParams params) {
    return authenticationService.authentication().flatMap(
        authn -> geekcollectionRepository.getGeekcollection(authn.buildBggRequestHeader(), params));
  }

}
