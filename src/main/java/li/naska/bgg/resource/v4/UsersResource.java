package li.naska.bgg.resource.v4;

import li.naska.bgg.repository.BggUsersV4Repository;
import li.naska.bgg.repository.model.BggUsersV4QueryParams;
import org.springdoc.api.annotations.ParameterObject;
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
  public Mono<String> getUser(@PathVariable Integer id) {
    return usersRepository.getUser(id);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getUsers(@ParameterObject @Validated BggUsersV4QueryParams params) {
    return usersRepository.getUsers(params);
  }

}
