package li.naska.bgg.graphql.service;

import com.boardgamegeek.user.v2.*;
import java.util.List;
import li.naska.bgg.graphql.model.enums.Domain;
import li.naska.bgg.repository.BggUserV2Repository;
import li.naska.bgg.repository.BggUsersV4Repository;
import li.naska.bgg.repository.model.BggUserV2QueryParams;
import li.naska.bgg.repository.model.BggUserV4ResponseBody;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GraphQLUsersService {

  private static final int BGG_USER_BUDDIES_PAGE_SIZE = 1000;

  private static final int BGG_USER_GUILDS_PAGE_SIZE = 1000;

  private final BggUserV2Repository userV2Repository;

  private final BggUsersV4Repository usersV4Repository;

  public GraphQLUsersService(
      BggUserV2Repository userV2Repository, BggUsersV4Repository usersV4Repository) {
    this.userV2Repository = userV2Repository;
    this.usersV4Repository = usersV4Repository;
  }

  public Mono<BggUserV4ResponseBody> getUser(Integer id) {
    return usersV4Repository.getUser(id);
  }

  public Mono<User> getUser(String username) {
    return getUser(username, Domain.boardgame, 1);
  }

  public Mono<User> getUser(String username, Domain domain, Integer page) {
    BggUserV2QueryParams queryParams = new BggUserV2QueryParams();
    queryParams.setName(username);
    queryParams.setGuilds(1);
    queryParams.setBuddies(1);
    queryParams.setTop(1);
    queryParams.setHot(1);
    queryParams.setDomain(domain.name());
    queryParams.setPage(page);
    return getUser(queryParams);
  }

  public Mono<List<Buddy>> getUserBuddies(String username) {
    return getUser(username, Domain.boardgame, 1).flatMap(user -> {
      int numPages = Math.max(
          1, (int) Math.ceil((double) user.getBuddies().getTotal() / BGG_USER_BUDDIES_PAGE_SIZE));
      return Flux.range(1, numPages)
          .flatMapSequential(page -> {
            if (page == 1) {
              return Mono.just(user);
            }
            return getUser(username, Domain.boardgame, page);
          })
          .map(User::getBuddies)
          .flatMapIterable(Buddies::getBuddies)
          .collectList();
    });
  }

  public Mono<List<Guild>> getUserGuilds(String username) {
    return getUser(username, Domain.boardgame, 1).flatMap(user -> {
      int numPages = Math.max(
          1, (int) Math.ceil((double) user.getGuilds().getTotal() / BGG_USER_GUILDS_PAGE_SIZE));
      return Flux.range(1, numPages)
          .flatMapSequential(page -> {
            if (page == 1) {
              return Mono.just(user);
            }
            return getUser(username, Domain.boardgame, page);
          })
          .map(User::getGuilds)
          .flatMapIterable(Guilds::getGuilds)
          .collectList();
    });
  }

  public Mono<Ranking> getUserRanking(String username, String type, Domain domain) {
    return getUser(username, domain, 1)
        .map(user -> "top".equals(type) ? user.getTop() : user.getHot());
  }

  private Mono<User> getUser(BggUserV2QueryParams queryParams) {
    return userV2Repository.getUser(queryParams);
  }
}
