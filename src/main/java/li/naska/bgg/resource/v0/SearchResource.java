package li.naska.bgg.resource.v0;

import li.naska.bgg.repository.BggSearchRepository;
import li.naska.bgg.repository.model.BggSearchParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v0/search")
public class SearchResource {

  @Autowired
  private BggSearchRepository searchRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getResultsAsXml(BggSearchParameters parameters) {
    return searchRepository.getResults(parameters);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getResultsAsJson(BggSearchParameters parameters) {
    return getResultsAsXml(parameters)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
