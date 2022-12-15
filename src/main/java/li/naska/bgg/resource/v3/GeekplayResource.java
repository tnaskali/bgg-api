package li.naska.bgg.resource.v3;

import li.naska.bgg.repository.BggGeekplayV2Repository;
import li.naska.bgg.repository.model.BggGeekplayV3RequestBody;
import li.naska.bgg.repository.model.BggGeekplayV3ResponseBody;
import li.naska.bgg.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("GeekplayV3Resource")
@RequestMapping("/api/v3/geekplay")
public class GeekplayResource {

  @Autowired
  private BggGeekplayV2Repository geekplayRepository;

  @Autowired
  private AuthenticationService authenticationService;

  @PostMapping(path = "/current", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<BggGeekplayV3ResponseBody> updateCurrentGeekplay(@Validated @RequestBody BggGeekplayV3RequestBody params) {
    return authenticationService.authentication().flatMap(
        authn -> geekplayRepository.updateGeekplay(authn.buildBggRequestHeader(), params));
  }

}
