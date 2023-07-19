package li.naska.bgg.graphql.controller;

import li.naska.bgg.graphql.data.UserBuddiesData;
import li.naska.bgg.graphql.data.UserData;
import li.naska.bgg.graphql.data.UserGuildsData;
import li.naska.bgg.graphql.model.Guild;
import li.naska.bgg.graphql.model.Ranking;
import li.naska.bgg.graphql.model.SimpleAddress;
import li.naska.bgg.graphql.model.User;
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
  public Mono<User> userByUsername(@Argument String username, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(username)).map(user -> new User(user.user().getName()));
  }

  @SchemaMapping
  public Mono<Integer> id(User user, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getId());
  }

  @SchemaMapping
  public Mono<String> lastname(User user, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getLastname().getValue());
  }

  @SchemaMapping
  public Mono<String> firstname(User user, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getFirstname().getValue());
  }

  @SchemaMapping
  public Mono<String> avatarlink(User user, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getAvatarlink().getValue())
        .filter(value -> !value.equals("N/A"));
  }

  @SchemaMapping
  public Mono<Integer> yearregistered(User user, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getYearregistered().getValue());
  }

  @SchemaMapping
  public Mono<LocalDate> lastlogin(User user, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getLastlogin().getValue());
  }

  @SchemaMapping
  public Mono<SimpleAddress> address(User user, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> new SimpleAddress(
            data.user().getStateorprovince().getValue(),
            data.user().getCountry().getValue())
        );
  }

  @SchemaMapping
  public Mono<String> webaddress(User user, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getWebaddress().getValue())
        .filter(value -> !value.equals(""));
  }

  @SchemaMapping
  public Mono<String> wiiaccount(User user, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getWiiaccount().getValue())
        .filter(value -> !value.equals(""));
  }

  @SchemaMapping
  public Mono<String> psnaccount(User user, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getPsnaccount().getValue())
        .filter(value -> !value.equals(""));
  }

  @SchemaMapping
  public Mono<String> battlenetaccount(User user, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getBattlenetaccount().getValue())
        .filter(value -> !value.equals(""));
  }

  @SchemaMapping
  public Mono<String> steamaccount(User user, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getSteamaccount().getValue())
        .filter(value -> !value.equals(""));
  }

  @SchemaMapping
  public Mono<String> xboxaccount(User user, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getXboxaccount().getValue())
        .filter(value -> !value.equals(""));
  }

  @SchemaMapping
  public Mono<Integer> marketrating(User user, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getMarketrating().getValue())
        .filter(value -> value != 0);
  }

  @SchemaMapping
  public Mono<Integer> traderating(User user, DataLoader<String, UserData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(data -> data.user().getTraderating().getValue())
        .filter(value -> value != 0);
  }

  @SchemaMapping
  public Mono<List<Guild>> guilds(User user, DataLoader<String, UserGuildsData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(UserGuildsData::guilds)
        .map(data -> data.stream().map(com.boardgamegeek.user.v2.Guild::getId).map(Guild::new).collect(Collectors.toList()));
  }

  @SchemaMapping
  public Mono<List<User>> buddies(User user, DataLoader<String, UserBuddiesData> loader) {
    return Mono.fromFuture(loader.load(user.username()))
        .map(UserBuddiesData::buddies)
        .map(data -> data.stream().map(com.boardgamegeek.user.v2.Buddy::getName).map(User::new).collect(Collectors.toList()));
  }

  @SchemaMapping
  public Mono<Ranking> top(User user) {
    return Mono.just(new Ranking(user, "top"));
  }

  @SchemaMapping
  public Mono<Ranking> hot(User user) {
    return Mono.just(new Ranking(user, "hot"));
  }

}
