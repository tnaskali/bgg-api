package li.naska.bgg.resource.v2;

import li.naska.bgg.repository.BggThingsRepository;
import li.naska.bgg.repository.model.BggThingsQueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v2/thing")
public class ThingResource {

  @Autowired
  private BggThingsRepository thingsRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getThingAsXml(@ParameterObject @Validated BggThingsQueryParams params) {
    return thingsRepository.getThings(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getThingAsJson(@ParameterObject @Validated BggThingsQueryParams params) {
    return getThingAsXml(params)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
