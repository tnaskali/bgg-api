package li.naska.bgg.resource.v2;

import com.boardgamegeek.thing.Things;
import li.naska.bgg.repository.BggThingV2Repository;
import li.naska.bgg.repository.model.BggThingV2QueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("ThingV2Resource")
@RequestMapping("/api/v2/thing")
public class ThingResource {

  @Autowired
  private BggThingV2Repository thingsRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getThingsAsXml(@ParameterObject @Validated BggThingV2QueryParams params) {
    return thingsRepository.getThings(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getThingsAsJson(@ParameterObject @Validated BggThingV2QueryParams params) {
    return getThingsAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, Things.class));
  }

}
