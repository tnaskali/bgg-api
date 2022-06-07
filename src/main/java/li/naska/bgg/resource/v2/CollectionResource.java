package li.naska.bgg.resource.v2;

import com.boardgamegeek.collection.Collection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggCollectionV2Repository;
import li.naska.bgg.repository.model.BggCollectionV2QueryParams;
import li.naska.bgg.service.AuthenticationService;
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
  private BggCollectionV2Repository collectionRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @Autowired
  private AuthenticationService authenticationService;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getCollectionAsXml(@ParameterObject @Validated BggCollectionV2QueryParams params) {
    return collectionRepository.getCollection(null, params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getCollectionAsJson(@ParameterObject @Validated BggCollectionV2QueryParams params) {
    return getCollectionAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, Collection.class));
  }

  @GetMapping(path = "/current", produces = MediaType.APPLICATION_XML_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<String> getCurrentCollectionAsXml(@ParameterObject @Validated BggCollectionV2QueryParams params) {
    return authenticationService.authentication().flatMap(
        authn -> collectionRepository.getCollection(authn.buildBggRequestHeader(), params));
  }

  @GetMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<String> getCurrentCollectionAsJson(@ParameterObject @Validated BggCollectionV2QueryParams params) {
    return getCurrentCollectionAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, Collection.class));
  }

}
