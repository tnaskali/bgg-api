package li.naska.bgg.resource.v2;

import com.boardgamegeek.plays.Plays;
import li.naska.bgg.repository.BggPlaysV2Repository;
import li.naska.bgg.repository.model.BggPlaysV2QueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController("PlaysV2Resource")
@RequestMapping("/api/v2/plays")
public class PlaysResource {

  @Autowired
  private BggPlaysV2Repository playsRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getPlaysAsXml(@Validated BggPlaysV2QueryParams params) {
    return playsRepository.getPlays(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getPlaysAsJson(@Validated BggPlaysV2QueryParams params) {
    return getPlaysAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, Plays.class));
  }

}
