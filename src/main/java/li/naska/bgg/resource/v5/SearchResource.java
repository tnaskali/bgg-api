package li.naska.bgg.resource.v5;

import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggSearchV5Repository;
import li.naska.bgg.repository.model.BggSearchV5QueryParams;
import li.naska.bgg.repository.model.BggSearchV5ResponseBody;
import li.naska.bgg.resource.v5.model.SearchDomain;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("SearchV5Resource")
@RequestMapping("/api/v5/search")
public class SearchResource {

  @Autowired
  private BggSearchV5Repository searchRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(path = "/{domain}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<BggSearchV5ResponseBody> getSearchResults(
      @NotNull @PathVariable SearchDomain domain,
      @Validated BggSearchV5QueryParams params) {
    return searchRepository.getSearchResults(domain, params);
  }

}
