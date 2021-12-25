package li.naska.bgg.resource.v2;

import li.naska.bgg.repository.BggFamiliesRepository;
import li.naska.bgg.repository.model.BggFamiliesQueryParams;
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
@RequestMapping("/api/v2/family")
public class FamilyResource {

  @Autowired
  private BggFamiliesRepository familiesRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getFamiliesAsXml(@ParameterObject @Validated BggFamiliesQueryParams params) {
    return familiesRepository.getFamilies(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getFamiliesAsJson(@ParameterObject @Validated BggFamiliesQueryParams params) {
    return getFamiliesAsXml(params)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
