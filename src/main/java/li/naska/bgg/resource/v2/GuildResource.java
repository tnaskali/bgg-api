package li.naska.bgg.resource.v2;

import com.boardgamegeek.guild.Guild;
import li.naska.bgg.repository.BggGuildsRepository;
import li.naska.bgg.repository.model.BggGuildQueryParams;
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
@RequestMapping("/api/v2/guild")
public class GuildResource {

  @Autowired
  private BggGuildsRepository guildsRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
  public Mono<String> getGuildAsXml(@ParameterObject @Validated BggGuildQueryParams params) {
    return guildsRepository.getGuild(params);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getGuildAsJson(@ParameterObject @Validated BggGuildQueryParams params) {
    return getGuildAsXml(params)
        .map(xml -> xmlProcessor.toJsonString(xml, Guild.class));
  }

}
