package li.naska.bgg.resource.v2;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import li.naska.bgg.repository.BggCollectionRepository;
import li.naska.bgg.repository.model.BggCollectionQueryParams;
import li.naska.bgg.security.BggAuthenticationToken;
import li.naska.bgg.util.XmlProcessor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/v2/collection")
public class CollectionResource {

  @Autowired
  private BggCollectionRepository collectionRepository;

  private Mono<BggAuthenticationToken> authentication() {
    return ReactiveSecurityContextHolder.getContext().map(
        context -> (BggAuthenticationToken) context.getAuthentication()
    );
  }

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getCollectionAsXml(@ParameterObject @Validated BggCollectionQueryParams parameters) {
    return collectionRepository.getCollection(null, parameters);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getCollectionAsJson(@ParameterObject @Validated BggCollectionQueryParams parameters) {
    return getCollectionAsXml(parameters)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

  @GetMapping(path = "/private", produces = MediaType.APPLICATION_XML_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<String> getPrivateCollectionAsXml(@ParameterObject @Validated BggCollectionQueryParams parameters) {
    return authentication().flatMap(
        authn -> collectionRepository.getCollection(
            Optional.ofNullable(authn).map(BggAuthenticationToken::buildBggRequestHeader).orElse(null),
            parameters));
  }

  @GetMapping(path = "/private", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(security = @SecurityRequirement(name = "basicAuth"))
  public Mono<String> getPrivateCollectionAsJson(@ParameterObject @Validated BggCollectionQueryParams parameters) {
    return getCollectionAsXml(parameters)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
