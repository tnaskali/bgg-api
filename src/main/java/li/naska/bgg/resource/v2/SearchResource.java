package li.naska.bgg.resource.v2;

import li.naska.bgg.repository.BggSearchRepository;
import li.naska.bgg.repository.model.BggSearchQueryParams;
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
@RequestMapping("/api/v2/search")
public class SearchResource {

  @Autowired
  private BggSearchRepository searchRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getResultsAsXml(@ParameterObject @Validated BggSearchQueryParams params) {
    return searchRepository.getResults(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getResultsAsJson(@ParameterObject @Validated BggSearchQueryParams params) {
    return getResultsAsXml(params)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
