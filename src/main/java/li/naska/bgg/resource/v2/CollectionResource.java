package li.naska.bgg.resource.v2;

import com.boardgamegeek.collection.Items;
import li.naska.bgg.repository.BggCollectionV2Repository;
import li.naska.bgg.repository.model.BggCollectionV2QueryParams;
import li.naska.bgg.service.AuthenticationService;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("CollectionV2Resource")
@RequestMapping("/api/v2/collection")
public class CollectionResource {

  @Autowired
  private BggCollectionV2Repository collectionRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @Autowired
  private AuthenticationService authenticationService;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getCollectionAsXml(@Validated BggCollectionV2QueryParams params) {
    return collectionRepository.getCollection(null, params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getCollectionAsJson(@Validated BggCollectionV2QueryParams params) {
    return getCollectionAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, Items.class));
  }

  @GetMapping(path = "/current", produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getCurrentCollectionAsXml(@Validated BggCollectionV2QueryParams params) {
    return authenticationService.authentication().flatMap(
        authn -> collectionRepository.getCollection(authn.buildBggRequestHeader(), params));
  }

  @GetMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getCurrentCollectionAsJson(@Validated BggCollectionV2QueryParams params) {
    return getCurrentCollectionAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, Items.class));
  }

}
