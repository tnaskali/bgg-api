package li.naska.bgg.resource.v2;

import com.boardgamegeek.search.Results;
import li.naska.bgg.repository.BggSearchV2Repository;
import li.naska.bgg.repository.model.BggSearchV2QueryParams;
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
  private BggSearchV2Repository searchRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getResultsAsXml(@ParameterObject @Validated BggSearchV2QueryParams params) {
    return searchRepository.getResults(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getResultsAsJson(@ParameterObject @Validated BggSearchV2QueryParams params) {
    return getResultsAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, Results.class));
  }

}
