package li.naska.bgg.resource.v2;

import com.boardgamegeek.plays.Plays;
import li.naska.bgg.repository.BggPlaysRepository;
import li.naska.bgg.repository.model.BggPlaysQueryParams;
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
@RequestMapping("/api/v2/plays")
public class PlaysResource {

  @Autowired
  private BggPlaysRepository playsRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getPlaysAsXml(@ParameterObject @Validated BggPlaysQueryParams params) {
    return playsRepository.getPlays(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getPlaysAsJson(@ParameterObject @Validated BggPlaysQueryParams params) {
    return getPlaysAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, Plays.class));
  }

}
