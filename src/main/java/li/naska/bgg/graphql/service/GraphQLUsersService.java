package li.naska.bgg.graphql.service;

import com.boardgamegeek.enums.UserDomainType;
import com.boardgamegeek.user.*;
import li.naska.bgg.repository.BggUserV2Repository;
import li.naska.bgg.repository.model.BggUserV2QueryParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GraphQLUsersService {

  private static final int BGG_USER_BUDDIES_PAGE_SIZE = 1000;

  private static final int BGG_USER_GUILDS_PAGE_SIZE = 1000;

  @Autowired
  private BggUserV2Repository usersRepository;

  @Autowired
  private XmlProcessor xmlProcessor;

  @Autowired
  private BatchLoaderRegistry registry;


  public Mono<User> getUser(String username) {
    return getUser(username, UserDomainType.BOARDGAME, 1);
  }

  public Mono<User> getUser(String username, UserDomainType domain, Integer page) {
    BggUserV2QueryParams queryParams = new BggUserV2QueryParams();
    queryParams.setName(username);
    queryParams.setGuilds(1);
    queryParams.setBuddies(1);
    queryParams.setTop(1);
    queryParams.setHot(1);
    queryParams.setDomain(domain.value());
    queryParams.setPage(page);
    return getUser(queryParams);
  }

  public Mono<List<Buddy>> getUserBuddies(String username) {
    return getUser(username, UserDomainType.BOARDGAME, 1)
        .flatMap(user -> {
          int numPages = Math.max(1, (int) Math.ceil((double) user.getBuddies().getTotal() / BGG_USER_BUDDIES_PAGE_SIZE));
          return Flux.range(1, numPages)
              .flatMapSequential(page -> {
                if (page == 1) {
                  return Mono.just(user);
                }
                return getUser(username, UserDomainType.BOARDGAME, page);
              })
              .map(User::getBuddies)
              .flatMapIterable(Buddies::getBuddies)
              .collect(Collectors.toList());
        });
  }

  public Mono<List<Guild>> getUserGuilds(String username) {
    return getUser(username, UserDomainType.BOARDGAME, 1)
        .flatMap(user -> {
          int numPages = Math.max(1, (int) Math.ceil((double) user.getGuilds().getTotal() / BGG_USER_GUILDS_PAGE_SIZE));
          return Flux.range(1, numPages)
              .flatMapSequential(page -> {
                if (page == 1) {
                  return Mono.just(user);
                }
                return getUser(username, UserDomainType.BOARDGAME, page);
              })
              .map(User::getGuilds)
              .flatMapIterable(Guilds::getGuilds)
              .collect(Collectors.toList());
        });
  }

  public Mono<Ranking> getUserRanking(String username, String type, UserDomainType domain) {
    return getUser(username, domain, 1).map(user -> "top".equals(type) ? user.getTop() : user.getHot());
  }

  private Mono<User> getUser(BggUserV2QueryParams queryParams) {
    return usersRepository.getUser(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, User.class));
  }

}
