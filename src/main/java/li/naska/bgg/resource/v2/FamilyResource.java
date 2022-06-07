package li.naska.bgg.resource.v2;

import com.boardgamegeek.family.Families;
import li.naska.bgg.repository.BggFamilyV2Repository;
import li.naska.bgg.repository.model.BggFamilyV2QueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("FamilyV2Resource")
@RequestMapping("/api/v2/family")
public class FamilyResource {

  @Autowired
  private BggFamilyV2Repository familiesRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getFamiliesAsXml(@ParameterObject @Validated BggFamilyV2QueryParams params) {
    return familiesRepository.getFamilies(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getFamiliesAsJson(@ParameterObject @Validated BggFamilyV2QueryParams params) {
    return getFamiliesAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, Families.class));
  }

}
