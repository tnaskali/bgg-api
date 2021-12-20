package li.naska.bgg.service;

import com.boardgamegeek.guild.Guild;
import li.naska.bgg.mapper.GuildsParamsMapper;
import li.naska.bgg.repository.BggGuildsRepository;
import li.naska.bgg.repository.model.BggGuildQueryParams;
import li.naska.bgg.resource.v3.model.GuildParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GuildsService {

  @Autowired
  private BggGuildsRepository guildsRepository;

  @Autowired
  private GuildsParamsMapper guildsParamsMapper;

  public Mono<Guild> getGuild(Integer id, GuildParams params) {
    BggGuildQueryParams bggParams = guildsParamsMapper.toBggModel(params);
    bggParams.setId(id);
    return guildsRepository.getGuild(bggParams)
        .map(xml -> new XmlProcessor(xml).toJavaObject(Guild.class));
  }

}
