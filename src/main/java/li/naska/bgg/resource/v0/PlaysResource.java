package li.naska.bgg.resource.v0;

import li.naska.bgg.repository.BggPlaysRepository;
import li.naska.bgg.repository.model.BggPlaysParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v0/plays")
public class PlaysResource {

  @Autowired
  private BggPlaysRepository playsRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getPlaysAsXml(BggPlaysParameters parameters) {
    return playsRepository.getPlays(parameters);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getPlaysAsJson(BggPlaysParameters parameters) {
    return getPlaysAsXml(parameters)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
