package li.naska.bgg.resource.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import li.naska.bgg.repository.BggPersonV1Repository;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("personV1Resource")
@RequestMapping("/api/v1/person")
public class PersonResource {

  private final BggPersonV1Repository personRepository;

  public PersonResource(BggPersonV1Repository personRepository) {
    this.personRepository = personRepository;
  }

  @GetMapping(
      value = "/{ids}",
      produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(
      summary = "Retrieve information about a particular person or persons",
      description =
          """
          Retrieve information about a particular person or persons.
          <p>
          <i>Syntax</i> : /person/{ids}
          <p>
          <i>Example</i> : /person/2
          <p>
          <i>Example</i> : /person/2,675
          """)
  public Mono<String> getPersons(
      @NotNull @PathVariable @Parameter(description = "The person id(s).", example = "[ 2, 675 ]")
          Set<Integer> ids,
      ServerHttpRequest request) {
    if (request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML)) {
      return personRepository.getPersonsAsXml(ids);
    } else {
      return personRepository.getPersonsAsJson(ids);
    }
  }
}
