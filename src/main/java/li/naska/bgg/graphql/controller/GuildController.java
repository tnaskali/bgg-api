package li.naska.bgg.graphql.controller;

import li.naska.bgg.graphql.data.GuildV2;
import li.naska.bgg.graphql.data.GuildV2Members;
import li.naska.bgg.graphql.data.UserV2;
import li.naska.bgg.graphql.model.Address;
import li.naska.bgg.graphql.model.Guild;
import li.naska.bgg.graphql.model.GuildMember;
import li.naska.bgg.graphql.model.User;
import li.naska.bgg.graphql.service.GraphQLGuildsService;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.ZonedDateTime;
import java.util.List;

@Controller("GraphQLGuildController")
public class GuildController {

  public GuildController(BatchLoaderRegistry registry, GraphQLGuildsService guildsService) {
    registry.forTypePair(Integer.class, GuildV2.class).registerMappedBatchLoader((ids, env) ->
        Flux.fromIterable(ids)
            .flatMap(id -> Mono.just(id).zipWith(guildsService.getGuild(id)))
            .collectMap(Tuple2::getT1, tuple -> new GuildV2(tuple.getT2()))
    );
    registry.forTypePair(Integer.class, GuildV2Members.class).registerMappedBatchLoader((ids, env) ->
        Flux.fromIterable(ids)
            .flatMap(id -> Mono.just(id).zipWith(guildsService.getMembers(id)))
            .collectMap(Tuple2::getT1, tuple -> new GuildV2Members(tuple.getT2()))
    );
  }

  @QueryMapping
  public Mono<Guild> guildById(@Argument Integer id, DataLoader<Integer, GuildV2> loader) {
    return Mono.fromFuture(loader.load(id))
        .map(guild -> new Guild(guild.guild().getId()));
  }

  @SchemaMapping
  public Mono<String> name(Guild guild, DataLoader<Integer, GuildV2> loader) {
    return Mono.fromFuture(loader.load(guild.id()))
        .map(data -> data.guild().getName());
  }

  @SchemaMapping
  public Mono<ZonedDateTime> created(Guild guild, DataLoader<Integer, GuildV2> loader) {
    return Mono.fromFuture(loader.load(guild.id()))
        .map(data -> data.guild().getCreated());
  }

  @SchemaMapping
  public Mono<String> category(Guild guild, DataLoader<Integer, GuildV2> loader) {
    return Mono.fromFuture(loader.load(guild.id())).map(data -> data.guild().getCategory());
  }

  @SchemaMapping
  public Mono<String> website(Guild guild, DataLoader<Integer, GuildV2> loader) {
    return Mono.fromFuture(loader.load(guild.id()))
        .map(data -> data.guild().getWebsite());
  }

  @SchemaMapping
  public Mono<String> description(Guild guild, DataLoader<Integer, GuildV2> loader) {
    return Mono.fromFuture(loader.load(guild.id()))
        .map(data -> data.guild().getDescription());
  }

  @SchemaMapping
  public Mono<User> manager(Guild guild, DataLoader<Integer, GuildV2> loader, DataLoader<String, UserV2> userLoader) {
    return Mono.fromFuture(loader.load(guild.id()))
        .flatMap(data -> Mono.fromFuture(userLoader.load(data.guild().getManager()))
            .map(user -> new User(user.user().getId(), user.user().getName())));
  }

  @SchemaMapping
  public Mono<Address> address(Guild guild, DataLoader<Integer, GuildV2> loader) {
    return Mono.fromFuture(loader.load(guild.id())).map(data -> new Address(
        data.guild().getLocation().getAddr1(),
        data.guild().getLocation().getAddr2(),
        data.guild().getLocation().getPostalcode(),
        data.guild().getLocation().getCity(),
        data.guild().getLocation().getStateorprovince(),
        null,
        data.guild().getLocation().getCountry()));
  }

  @SchemaMapping
  public Mono<List<GuildMember>> members(Guild guild, DataLoader<Integer, GuildV2Members> loader, DataLoader<String, UserV2> userLoader) {
    return Mono.fromFuture(loader.load(guild.id()))
        .flatMapIterable(GuildV2Members::members)
        .flatMap(member -> Mono.fromFuture(userLoader.load(member.getName()))
            .map(user -> new GuildMember(new User(user.user().getId(), user.user().getName()), member.getDate())))
        .collectList();
  }

}
