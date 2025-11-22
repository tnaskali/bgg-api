package li.naska.bgg.resource.v2;

import io.swagger.v3.oas.annotations.Operation;
import li.naska.bgg.repository.BggPersonV2Repository;
import li.naska.bgg.repository.model.BggPersonV2QueryParams;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("personV2Resource")
@RequestMapping("/api/v2/person")
public class PersonResource {

  private final BggPersonV2Repository personRepository;

  public PersonResource(BggPersonV2Repository personRepository) {
    this.personRepository = personRepository;
  }

  @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
  @Operation(summary = "Person items", description = """
          In the BGG database, any real person is called a person.
          <p>
          <i>Syntax</i> : /person?id={ids}[&{parameters}]
          <p>
          <i>Example</i> : /person?id=153580,150831
          """)
  public Mono<String> getPerson(
      @Validated @ParameterObject BggPersonV2QueryParams params, ServerHttpRequest request) {
    if (request.getHeaders().getAccept().contains(MediaType.APPLICATION_XML)) {
      return personRepository.getItemsAsXml(params);
    } else {
      return personRepository.getItemsAsJson(params);
    }
  }
}
