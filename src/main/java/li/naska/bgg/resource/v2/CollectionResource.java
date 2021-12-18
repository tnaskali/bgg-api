package li.naska.bgg.resource.v2;

import li.naska.bgg.repository.BggCollectionRepository;
import li.naska.bgg.repository.model.BggCollectionQueryParams;
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
@RequestMapping("/api/v2/collection")
public class CollectionResource {

  @Autowired
  private BggCollectionRepository collectionRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getCollectionAsXml(@ParameterObject @Validated BggCollectionQueryParams parameters) {
    return collectionRepository.getCollection(parameters);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getCollectionAsJson(@ParameterObject @Validated BggCollectionQueryParams parameters) {
    return getCollectionAsXml(parameters)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
