package li.naska.bgg.graphql.service;

import com.boardgamegeek.xml.guild.v2.Guild;
import com.boardgamegeek.xml.guild.v2.Member;
import com.boardgamegeek.xml.guild.v2.Members;
import java.util.List;
import li.naska.bgg.repository.BggGuildV2Repository;
import li.naska.bgg.repository.model.BggGuildV2QueryParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GraphQLGuildsService {

  private static final int BGG_GUILD_MEMBERS_PAGE_SIZE = 25;

  private final BggGuildV2Repository guildV2Repository;

  public GraphQLGuildsService(BggGuildV2Repository guildV2Repository) {
    this.guildV2Repository = guildV2Repository;
  }

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
    return getGuild(id, 1).flatMap(guild -> {
      int numPages =
          (int) Math.ceil((double) guild.getMembers().getCount() / BGG_GUILD_MEMBERS_PAGE_SIZE);
      return Flux.range(1, numPages)
          .flatMapSequential(page -> {
            if (page == 1) {
              return Mono.just(guild);
            }
            return getGuild(id, page);
          })
          .map(Guild::getMembers)
          .flatMapIterable(Members::getMembers)
          .collectList();
    });
  }

  private Mono<Guild> getGuild(BggGuildV2QueryParams queryParams) {
    return guildV2Repository.getGuild(queryParams);
  }
}
