package li.naska.bgg.resource.v4;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import li.naska.bgg.repository.BggUsersV4Repository;
import li.naska.bgg.repository.model.BggUsersV4QueryParams;
import li.naska.bgg.repository.model.BggUsersV4ResponseBody;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("UsersV4Resource")
@RequestMapping("/api/v4/users")
public class UsersResource {

  @Autowired
  private BggUsersV4Repository usersRepository;

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get user by id",
      description =
          """
          Get user by id.
          <p>
          <i>Syntax</i> : /users/{id}
          <p>
          <i>Example</i> : /users/10937
          """)
  public Mono<BggUsersV4ResponseBody> getUser(
      @PathVariable @Parameter(example = "10937", description = "User id.") Integer id) {
    return usersRepository.getUser(id);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
      summary = "Get user by username",
      description =
          """
          Get user by username.
          <p>
          <i>Syntax</i> : /users?username={username}
          <p>
          <i>Example</i> : /users?username=Jester
          """)
  public Mono<List<BggUsersV4ResponseBody>> getUsers(
      @Validated @ParameterObject BggUsersV4QueryParams params) {
    return usersRepository.getUsers(params);
  }
}
