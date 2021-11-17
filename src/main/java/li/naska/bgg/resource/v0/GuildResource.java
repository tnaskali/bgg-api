package li.naska.bgg.resource.v0;

import li.naska.bgg.repository.BggGuildRepository;
import li.naska.bgg.repository.model.BggGuildParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v0/guild")
public class GuildResource {

  @Autowired
  private BggGuildRepository guildsRepository;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getGuildAsXml(BggGuildParameters parameters) {
    return guildsRepository.getGuild(parameters);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getGuildAsJson(BggGuildParameters parameters) {
    return getGuildAsXml(parameters)
        .map(xml -> new XmlProcessor(xml).toJsonString());
  }

}
