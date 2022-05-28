package li.naska.bgg.resource.v2;

import li.naska.bgg.repository.BggUnifiedSearchRepository;
import li.naska.bgg.repository.model.BggUnifiedSearchQueryParams;
import li.naska.bgg.repository.model.BggUnifiedSearchResponseBody;
import li.naska.bgg.resource.v2.model.UnifiedSearchDomain;
import li.naska.bgg.util.XmlProcessor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v2/unifiedsearch")
public class UnifiedSearchResource {

  @Autowired
  private BggUnifiedSearchRepository searchRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(path = "/{domain}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<BggUnifiedSearchResponseBody> getSearchResults(
      @NotNull @PathVariable UnifiedSearchDomain domain,
      @ParameterObject @Validated BggUnifiedSearchQueryParams params) {
    return searchRepository.getSearchResults(domain, params);
  }

}
