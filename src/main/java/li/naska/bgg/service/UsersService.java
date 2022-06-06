package li.naska.bgg.service;

import com.boardgamegeek.enums.UserDomainType;
import li.naska.bgg.mapper.UserMapper;
import li.naska.bgg.repository.BggUsersRepository;
import li.naska.bgg.repository.model.BggUserQueryParams;
import li.naska.bgg.resource.vN.model.Guild;
import li.naska.bgg.resource.vN.model.User;
import li.naska.bgg.resource.vN.model.User.Buddy;
import li.naska.bgg.resource.vN.model.User.Ranking.RankedItem;
import li.naska.bgg.resource.vN.model.UserRankedItemsParams;
import li.naska.bgg.util.Page;
import li.naska.bgg.util.PagingHelper;
import li.naska.bgg.util.PagingParams;
import li.naska.bgg.util.XmlProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersService {

  private static final int BGG_USER_BUDDIES_PAGE_SIZE = 1000;

  private static final int BGG_USER_GUILDS_PAGE_SIZE = 1000;

  @Autowired
  private BggUsersRepository usersRepository;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private XmlProcessor xmlProcessor;

  public Mono<User> getUser(String username) {
    BggUserQueryParams queryParams = new BggUserQueryParams();
    queryParams.setName(username);
    return getUser(queryParams);
  }

  public Mono<List<Buddy>> getBuddies(String username) {
    BggUserQueryParams firstPageQueryParams = new BggUserQueryParams();
    firstPageQueryParams.setName(username);
    firstPageQueryParams.setBuddies(1);
    firstPageQueryParams.setPage(1);
    return getUser(firstPageQueryParams)
        .flatMap(user -> {
          int numPages = (int) Math.ceil((double) user.getNumbuddies() / BGG_USER_BUDDIES_PAGE_SIZE);
          return Flux.range(1, numPages)
              .flatMapSequential(page -> {
                if (page == 1) {
                  return Mono.just(user);
                }
                BggUserQueryParams queryParams = new BggUserQueryParams();
                queryParams.setName(username);
                queryParams.setBuddies(1);
                queryParams.setPage(page);
                return getUser(queryParams);
              })
              .flatMapIterable(User::getBuddies)
              .collect(Collectors.toList());
        });
  }

  public Mono<Page<Buddy>> getPagedBuddies(String username, PagingParams pagingParams) {
    PagingHelper helper = new PagingHelper(
        pagingParams.getSize(),
        pagingParams.getPage(),
        BGG_USER_BUDDIES_PAGE_SIZE);
    BggUserQueryParams firstPageQueryParams = new BggUserQueryParams();
    firstPageQueryParams.setName(username);
    firstPageQueryParams.setBuddies(1);
    firstPageQueryParams.setPage(helper.getBggStartPage());
    return getUser(firstPageQueryParams)
        .flatMap(user -> helper.getBggPagesRange(user.getNumbuddies())
            .flatMapSequential(page -> {
              if (page == helper.getBggStartPage()) {
                return Mono.just(user);
              }
              BggUserQueryParams queryParams = new BggUserQueryParams();
              queryParams.setName(username);
              queryParams.setBuddies(1);
              queryParams.setPage(page);
              return getUser(queryParams);
            })
            .flatMapIterable(User::getBuddies)
            .collect(Collectors.toList())
            .map(list -> helper.buildPage(list, user.getNumbuddies()))
        );
  }

  public Mono<List<Guild>> getGuilds(String username) {
    BggUserQueryParams firstPageQueryParams = new BggUserQueryParams();
    firstPageQueryParams.setName(username);
    firstPageQueryParams.setGuilds(1);
    firstPageQueryParams.setPage(1);
    return getUser(firstPageQueryParams)
        .flatMap(user -> {
          int numPages = (int) Math.ceil((double) user.getNumguilds() / BGG_USER_BUDDIES_PAGE_SIZE);
          return Flux.range(1, numPages)
              .flatMapSequential(page -> {
                if (page == 1) {
                  return Mono.just(user);
                }
                BggUserQueryParams queryParams = new BggUserQueryParams();
                queryParams.setName(username);
                queryParams.setGuilds(1);
                queryParams.setPage(page);
                return getUser(queryParams);
              })
              .flatMapIterable(User::getGuilds)
              .collect(Collectors.toList());
        });
  }

  public Mono<Page<Guild>> getPagedGuilds(String username, PagingParams pagingParams) {
    PagingHelper helper = new PagingHelper(
        pagingParams.getSize(),
        pagingParams.getPage(),
        BGG_USER_GUILDS_PAGE_SIZE);
    BggUserQueryParams firstPageQueryParams = new BggUserQueryParams();
    firstPageQueryParams.setName(username);
    firstPageQueryParams.setGuilds(1);
    firstPageQueryParams.setPage(helper.getBggStartPage());
    return getUser(firstPageQueryParams)
        .flatMap(user -> helper.getBggPagesRange(user.getNumguilds())
            .flatMapSequential(page -> {
              if (page == helper.getBggStartPage()) {
                return Mono.just(user);
              }
              BggUserQueryParams queryParams = new BggUserQueryParams();
              queryParams.setName(username);
              queryParams.setGuilds(1);
              queryParams.setPage(page);
              return getUser(queryParams);
            })
            .flatMapIterable(User::getGuilds)
            .collect(Collectors.toList())
            .map(list -> helper.buildPage(list, user.getNumguilds()))
        );
  }

  public Mono<List<RankedItem>> getHotItems(String username, UserRankedItemsParams params) {
    BggUserQueryParams queryParams = new BggUserQueryParams();
    queryParams.setName(username);
    queryParams.setHot(1);
    queryParams.setDomain(Optional.ofNullable(params.getDomain()).map(UserDomainType::value).orElse(null));
    return getUser(queryParams)
        .map(user -> Optional.ofNullable(user.getHot()).map(User.Ranking::getItems).orElse(Collections.emptyList()));
  }

  public Mono<List<RankedItem>> getTopItems(String username, UserRankedItemsParams params) {
    BggUserQueryParams queryParams = new BggUserQueryParams();
    queryParams.setName(username);
    queryParams.setTop(1);
    queryParams.setDomain(Optional.ofNullable(params.getDomain()).map(UserDomainType::value).orElse(null));
    return getUser(queryParams)
        .map(user -> Optional.ofNullable(user.getTop()).map(User.Ranking::getItems).orElse(Collections.emptyList()));
  }

  private Mono<User> getUser(BggUserQueryParams queryParams) {
    return usersRepository.getUser(queryParams)
        .map(xml -> xmlProcessor.toJavaObject(xml, com.boardgamegeek.user.User.class))
        .map(userMapper::fromBggModel);
  }
}
