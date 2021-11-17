package li.naska.bgg.resource.v0;

import li.naska.bgg.repository.BggCollectionRepository;
import li.naska.bgg.repository.model.BggCollectionParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v0/collection")
public class CollectionResource {

  @Autowired
  private BggCollectionRepository collectionRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getCollectionAsXml(BggCollectionParameters parameters) {
    return collectionRepository.getCollection(parameters);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getCollectionAsJson(BggCollectionParameters parameters) {
    return getCollectionAsXml(parameters)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
