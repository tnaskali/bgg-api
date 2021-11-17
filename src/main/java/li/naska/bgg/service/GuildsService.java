package li.naska.bgg.service;

import com.boardgamegeek.guild.Guild;
import li.naska.bgg.repository.BggGuildRepository;
import li.naska.bgg.repository.model.BggGuildParameters;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GuildsService {

  @Autowired
  private BggGuildRepository guildRepository;

  public Mono<Guild> getGuild(BggGuildParameters parameters) {
    return guildRepository.getGuild(parameters)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Guild.class));
  }

}
