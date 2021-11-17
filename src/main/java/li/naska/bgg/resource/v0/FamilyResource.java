package li.naska.bgg.resource.v0;

import li.naska.bgg.repository.BggFamilyRepository;
import li.naska.bgg.repository.model.BggFamilyParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v0/family")
public class FamilyResource {

  @Autowired
  private BggFamilyRepository familiesRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getFamiliesAsXml(BggFamilyParameters parameters) {
    return familiesRepository.getFamily(parameters);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getFamiliesAsJson(BggFamilyParameters parameters) {
    return getFamiliesAsXml(parameters)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
