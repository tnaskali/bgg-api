package li.naska.bgg.resource.v1;

import com.boardgamegeek.geeklist.Geeklist;
import jakarta.validation.constraints.NotNull;
import li.naska.bgg.repository.BggGeeklistV1Repository;
import li.naska.bgg.repository.model.BggGeeklistV1QueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("GeeklistV1Resource")
@RequestMapping("/api/v1/geeklist")
public class GeeklistResource {

  @Autowired
  private BggGeeklistV1Repository geeklistRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getGeeklistAsXml(
      @NotNull @PathVariable Integer id,
      @Validated BggGeeklistV1QueryParams params) {
    return geeklistRepository.getGeeklist(id, params);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getGeeklistAsJson(
      @NotNull @PathVariable Integer id,
      @Validated BggGeeklistV1QueryParams params) {
    return getGeeklistAsXml(id, params)
        .map(xml -> xmlProcessor.toJsonString(xml, Geeklist.class));
  }

}
