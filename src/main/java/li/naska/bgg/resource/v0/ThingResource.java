package li.naska.bgg.resource.v0;

import li.naska.bgg.repository.BggThingRepository;
import li.naska.bgg.repository.model.BggThingsParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v0/thing")
public class ThingResource {

  @Autowired
  private BggThingRepository thingsRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getThingAsXml(BggThingsParameters parameters) {
    return thingsRepository.getThings(parameters);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getThingAsJson(BggThingsParameters parameters) {
    return getThingAsXml(parameters)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
