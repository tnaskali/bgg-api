package li.naska.bgg.resource.v1;

import com.boardgamegeek.guild.Guild;
import li.naska.bgg.repository.model.BggGuildParameters;
import li.naska.bgg.service.GuildsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/guilds")
public class GuildsResource {

  @Autowired
  private GuildsService guildsService;

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Guild> getGuild(BggGuildParameters parameters) {
    return guildsService.getGuild(parameters);
  }

}
