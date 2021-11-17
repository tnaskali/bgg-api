package li.naska.bgg.resource.v0;

import li.naska.bgg.repository.BggUserRepository;
import li.naska.bgg.repository.model.BggUserParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v0/user")
public class UserResource {

  @Autowired
  private BggUserRepository usersRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getUserAsXml(BggUserParameters parameters) {
    return usersRepository.getUser(parameters);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getUserAsJson(BggUserParameters parameters) {
    return getUserAsXml(parameters)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
