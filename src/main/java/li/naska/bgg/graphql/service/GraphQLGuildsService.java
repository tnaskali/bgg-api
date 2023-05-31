package li.naska.bgg.graphql.service;

import com.boardgamegeek.guild.Guild;
import com.boardgamegeek.guild.Member;
import com.boardgamegeek.guild.Members;
import li.naska.bgg.repository.BggGuildV2Repository;
import li.naska.bgg.repository.model.BggGuildV2QueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GraphQLGuildsService {

  private static final int BGG_GUILD_MEMBERS_PAGE_SIZE = 25;

  @Autowired
  private BggGuildV2Repository guildsRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<Guild> getGuild(Integer id) {
    return getGuild(id, 1);
  }

  public Mono<Guild> getGuild(Integer id, Integer page) {
    BggGuildV2QueryParams queryParams = new BggGuildV2QueryParams();
    queryParams.setId(id);
    queryParams.setMembers(1);
    queryParams.setPage(page);
    return getGuild(queryParams);
  }

  public Mono<List<Member>> getMembers(Integer id) {
    return getGuild(id, 1)
        .flatMap(guild -> {
          int numPages = (int) Math.ceil((double) guild.getMembers().getCount() / BGG_GUILD_MEMBERS_PAGE_SIZE);
          return Flux.range(1, numPages)
              .flatMapSequential(page -> {
                if (page == 1) {
                  return Mono.just(guild);
                }
                return getGuild(id, page);
              })
              .map(Guild::getMembers)
              .flatMapIterable(Members::getMembers)
              .collect(Collectors.toList());
        });
  }

  private Mono<Guild> getGuild(BggGuildV2QueryParams queryParams) {
    return guildsRepository.getGuild(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, Guild.class));
  }

}
