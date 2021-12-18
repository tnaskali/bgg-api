package li.naska.bgg.resource.v1;

import li.naska.bgg.repository.BggGeeklistsRepository;
import li.naska.bgg.repository.model.BggGeeklistQueryParams;
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
@RequestMapping("/api/v1/geeklist")
public class GeeklistResource {

  @Autowired
  private BggGeeklistsRepository geeklistsRepository;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getGeeklistAsXml(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated BggGeeklistQueryParams params) {
    return geeklistsRepository.getGeeklist(id, params);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getGeeklistAsJson(
      @NotNull @PathVariable Integer id,
      @ParameterObject @Validated BggGeeklistQueryParams params) {
    return getGeeklistAsXml(id, params)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
