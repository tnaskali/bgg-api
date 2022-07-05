package li.naska.bgg.resource.v5;

import li.naska.bgg.service.LoginService;
import li.naska.bgg.service.model.LoginParams;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController("LoginV5Resource")
@RequestMapping("/api/v5/login")
public class LoginResource {

  @Autowired
  private LoginService loginService;

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<List<String>> getSearchResults(
      @ParameterObject @Validated LoginParams params) {
    return loginService.login(params.getUsername(), params.getPassword());
  }

}
