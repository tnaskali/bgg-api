package li.naska.bgg.graphql.controller;

import li.naska.bgg.graphql.data.UserV2Buddies;
import li.naska.bgg.graphql.data.UserV2;
import li.naska.bgg.graphql.data.UserV2Guilds;
import li.naska.bgg.graphql.data.UserV4;
import li.naska.bgg.graphql.model.*;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller("GraphQLUserController")
public class UserController {

  @QueryMapping
  public Mono<User> userById(@Argument Integer id, DataLoader<Integer, UserV4> loader) {
    return Mono.fromFuture(loader.load(id)).map(user -> new User(user.user().getId(), user.user().getUsername()));
  }

  @QueryMapping
  public Mono<User> userByUsername(@Argument String username, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(username)).map(user -> new User(user.user().getId(), user.user().getName()));
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
        .map(data -> data.user().getWebaddress().getValue())
        .filter(value -> !value.isEmpty());
  }

  @SchemaMapping
  public Mono<String> wiiaccount(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getWiiaccount().getValue())
        .filter(value -> !value.isEmpty());
  }

  @SchemaMapping
  public Mono<String> psnaccount(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getPsnaccount().getValue())
        .filter(value -> !value.isEmpty());
  }

  @SchemaMapping
  public Mono<String> battlenetaccount(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getBattlenetaccount().getValue())
        .filter(value -> !value.isEmpty());
  }

  @SchemaMapping
  public Mono<String> steamaccount(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getSteamaccount().getValue())
        .filter(value -> !value.isEmpty());
  }

  @SchemaMapping
  public Mono<String> xboxaccount(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getXboxaccount().getValue())
        .filter(value -> !value.isEmpty());
  }

  @SchemaMapping
  public Mono<Integer> marketrating(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getMarketrating().getValue())
        .filter(value -> value != 0);
  }

  @SchemaMapping
  public Mono<Integer> traderating(User user, DataLoader<String, UserV2> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getTraderating().getValue())
        .filter(value -> value != 0);
  }

  @SchemaMapping
  public Mono<List<Guild>> guilds(User user, DataLoader<String, UserV2Guilds> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(UserV2Guilds::guilds)
        .map(data -> data.stream().map(com.boardgamegeek.user.v2.Guild::getId).map(Guild::new).collect(Collectors.toList()));
  }

  @SchemaMapping
  public Mono<List<User>> buddies(User user, DataLoader<String, UserV2Buddies> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(UserV2Buddies::buddies)
        .map(data -> data.stream().map(buddy -> new User(buddy.getId(), buddy.getName())).collect(Collectors.toList()));
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
        .map(data -> new Address(null, null, null, data.user().getCity(), data.user().getState(), data.user().getIsocountry(), data.user().getCountry()));
  }

  @SchemaMapping
  public Mono<LocalDate> dateregistered(User user, DataLoader<Integer, UserV4> loader) {
    return Mono.fromFuture(loader.load(user.id()))
        .map(data -> data.user().getRegdate());
  }

  @SchemaMapping
  public Mono<List<Integer>> supportyears(User user, DataLoader<Integer, UserV4> loader) {
    return Mono.fromFuture(loader.load(user.id()))
        .map(data -> data.user().getSupportYears());
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
