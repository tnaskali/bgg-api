package li.naska.bgg.graphql.controller;

import com.boardgamegeek.common.IntegerValue;
import com.boardgamegeek.common.StringValue;
import java.time.LocalDate;
import java.util.List;
import li.naska.bgg.graphql.data.UserV2;
import li.naska.bgg.graphql.data.UserV2Buddies;
import li.naska.bgg.graphql.data.UserV2Guilds;
import li.naska.bgg.graphql.data.UserV4;
import li.naska.bgg.graphql.model.*;
import li.naska.bgg.graphql.service.GraphQLUsersService;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Controller("graphQLUserController")
public class UserController {

  public UserController(BatchLoaderRegistry registry, GraphQLUsersService usersService) {
    registry
        .forTypePair(Integer.class, UserV4.class)
        .registerMappedBatchLoader((ids, env) -> Flux.fromIterable(ids)
            .flatMap(id -> Mono.just(id).zipWith(usersService.getUser(id)))
            .collectMap(Tuple2::getT1, tuple -> new UserV4(tuple.getT2())));
    registry
        .forTypePair(String.class, UserV2.class)
        .registerMappedBatchLoader((usernames, env) -> Flux.fromIterable(usernames)
            .flatMap(username -> Mono.just(username).zipWith(usersService.getUser(username)))
            .collectMap(Tuple2::getT1, tuple -> new UserV2(tuple.getT2())));
    registry
        .forTypePair(String.class, UserV2Guilds.class)
        .registerMappedBatchLoader((usernames, env) -> Flux.fromIterable(usernames)
            .flatMap(username -> Mono.just(username).zipWith(usersService.getUserGuilds(username)))
            .collectMap(Tuple2::getT1, tuple -> new UserV2Guilds(tuple.getT2())));
    registry
        .forTypePair(String.class, UserV2Buddies.class)
        .registerMappedBatchLoader((usernames, env) -> Flux.fromIterable(usernames)
            .flatMap(username -> Mono.just(username).zipWith(usersService.getUserBuddies(username)))
            .collectMap(Tuple2::getT1, tuple -> new UserV2Buddies(tuple.getT2())));
  }

  @QueryMapping
  public Mono<User> userById(@Argument Integer id, DataLoader<Integer, UserV4> loader) {
    return Mono.fromFuture(loader.load(id))
        .map(data -> new User(data.user().getId(), data.user().getUsername()));
  }

  @QueryMapping
  public Mono<User> userByUsername(@Argument String username, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(username))
        .map(data -> new User(data.user().getId(), data.user().getName()));
  }

  @SchemaMapping
  public Mono<String> lastname(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getLastname().getValue());
  }

  @SchemaMapping
  public Mono<String> firstname(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getFirstname().getValue());
  }

  @SchemaMapping
  public Mono<String> avatarlink(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getAvatarlink().getValue())
        .filter(value -> !value.equals("N/A"));
  }

  @SchemaMapping
  public Mono<LocalDate> lastlogin(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getLastlogin().getValue());
  }

  @SchemaMapping
  public Mono<String> webaddress(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .mapNotNull(data -> data.user().getWebaddress())
        .map(StringValue::getValue)
        .filter(value -> !value.isEmpty());
  }

  @SchemaMapping
  public Mono<String> wiiaccount(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .mapNotNull(data -> data.user().getWiiaccount())
        .map(StringValue::getValue)
        .filter(value -> !value.isEmpty());
  }

  @SchemaMapping
  public Mono<String> psnaccount(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .mapNotNull(data -> data.user().getPsnaccount())
        .map(StringValue::getValue)
        .filter(value -> !value.isEmpty());
  }

  @SchemaMapping
  public Mono<String> battlenetaccount(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .mapNotNull(data -> data.user().getBattlenetaccount())
        .map(StringValue::getValue)
        .filter(value -> !value.isEmpty());
  }

  @SchemaMapping
  public Mono<String> steamaccount(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .mapNotNull(data -> data.user().getSteamaccount())
        .map(StringValue::getValue)
        .filter(value -> !value.isEmpty());
  }

  @SchemaMapping
  public Mono<String> xboxaccount(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .mapNotNull(data -> data.user().getXboxaccount())
        .map(StringValue::getValue)
        .filter(value -> !value.isEmpty());
  }

  @SchemaMapping
  public Mono<Integer> marketrating(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .mapNotNull(data -> data.user().getMarketrating())
        .map(IntegerValue::getValue)
        .filter(value -> value != 0);
  }

  @SchemaMapping
  public Mono<Integer> traderating(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .mapNotNull(data -> data.user().getTraderating())
        .map(IntegerValue::getValue)
        .filter(value -> value != 0);
  }

  @SchemaMapping
  public Mono<List<Guild>> guilds(User user, DataLoader<String, UserV2Guilds> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(UserV2Guilds::guilds)
        .map(data -> data.stream()
            .map(com.boardgamegeek.user.v2.Guild::getId)
            .map(Guild::new)
            .toList());
  }

  @SchemaMapping
  public Mono<List<User>> buddies(User user, DataLoader<String, UserV2Buddies> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(UserV2Buddies::buddies)
        .map(data ->
            data.stream().map(buddy -> new User(buddy.getId(), buddy.getName())).toList());
  }

  @SchemaMapping
  public Mono<Ranking> top(User user) {
    return Mono.just(new Ranking(user, "top"));
  }

  @SchemaMapping
  public Mono<Ranking> hot(User user) {
    return Mono.just(new Ranking(user, "hot"));
  }

  @SchemaMapping
  public Mono<Address> address(User user, DataLoader<Integer, UserV4> loader) {
    return Mono.fromFuture(loader.load(user.id()))
        .map(data -> new Address(
            null,
            null,
            null,
            data.user().getCity(),
            data.user().getState(),
            data.user().getIsocountry(),
            data.user().getCountry()));
  }

  @SchemaMapping
  public Mono<LocalDate> dateregistered(User user, DataLoader<Integer, UserV4> loader) {
    return Mono.fromFuture(loader.load(user.id())).map(data -> data.user().getRegdate());
  }

  @SchemaMapping
  public Mono<List<Integer>> supportyears(User user, DataLoader<Integer, UserV4> loader) {
    return Mono.fromFuture(loader.load(user.id())).map(data -> data.user().getSupportYears());
  }

  @SchemaMapping
  public Mono<Integer> designerid(User user, DataLoader<Integer, UserV4> loader) {
    return Mono.fromFuture(loader.load(user.id()))
        .map(data -> data.user().getDesignerid())
        .filter(value -> value != 0);
  }

  @SchemaMapping
  public Mono<Integer> publisherid(User user, DataLoader<Integer, UserV4> loader) {
    return Mono.fromFuture(loader.load(user.id()))
        .map(data -> data.user().getPublisherid())
        .filter(value -> value != 0);
  }

  @SchemaMapping
  public Mono<List<Microbadge>> microbadges(User user, DataLoader<Integer, UserV4> loader) {
    return Mono.fromFuture(loader.load(user.id()))
        .flatMapIterable(data -> data.user().getUserMicrobadges())
        .map(microbadge -> new Microbadge(microbadge.getBadgeid()))
        .collectList();
  }
}
