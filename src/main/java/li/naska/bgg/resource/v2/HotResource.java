package li.naska.bgg.resource.v2;

import li.naska.bgg.repository.BggHotItemsRepository;
import li.naska.bgg.repository.model.BggHotItemsQueryParams;
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
@RequestMapping("/api/v2/hot")
public class HotResource {

  @Autowired
  private BggHotItemsRepository hotItemsRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getHotItemsAsXml(@ParameterObject @Validated BggHotItemsQueryParams params) {
    return hotItemsRepository.getHotItems(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getHotItemsAsJson(@ParameterObject @Validated BggHotItemsQueryParams params) {
    return getHotItemsAsXml(params)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}