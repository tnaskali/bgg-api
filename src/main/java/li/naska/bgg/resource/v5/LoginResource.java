package li.naska.bgg.resource.v5;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import li.naska.bgg.service.LoginService;
import li.naska.bgg.service.model.LoginParams;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("loginV5Resource")
@RequestMapping("/api/v5/login")
public class LoginResource {

  private final LoginService loginService;

  public LoginResource(LoginService loginService) {
    this.loginService = loginService;
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Login",
      description =
          """
          Login to BGG.
          <p>
          <i>Syntax</i> : /login
          """)
  public Mono<List<String>> login(@Validated @RequestBody LoginParams params) {
    return loginService.login(params.getUsername(), params.getPassword());
  }
}
