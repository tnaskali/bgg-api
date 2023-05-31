package li.naska.bgg.graphql.controller;

import li.naska.bgg.graphql.data.GuildData;
import li.naska.bgg.graphql.data.GuildMembersData;
import li.naska.bgg.graphql.model.Address;
import li.naska.bgg.graphql.model.Guild;
import li.naska.bgg.graphql.model.GuildMember;
import li.naska.bgg.graphql.model.User;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller("GraphQLGuildController")
public class GuildController {

  @QueryMapping
  public Mono<Guild> guildById(@Argument Integer id, DataLoader<Integer, GuildData> loader) {
    return Mono.fromFuture(loader.load(id)).map(guild -> new Guild(guild.guild().getId()));
  }

  @SchemaMapping
  public Mono<String> name(Guild guild, DataLoader<Integer, GuildData> loader) {
    return Mono.fromFuture(loader.load(guild.id())).map(data -> data.guild().getName());
  }

  @SchemaMapping
  public Mono<ZonedDateTime> created(Guild guild, DataLoader<Integer, GuildData> loader) {
    return Mono.fromFuture(loader.load(guild.id())).map(data -> data.guild().getCreated());
  }

  @SchemaMapping
  public Mono<String> category(Guild guild, DataLoader<Integer, GuildData> loader) {
    return Mono.fromFuture(loader.load(guild.id())).map(data -> data.guild().getCategory());
  }

  @SchemaMapping
  public Mono<String> website(Guild guild, DataLoader<Integer, GuildData> loader) {
    return Mono.fromFuture(loader.load(guild.id())).map(data -> data.guild().getWebsite());
  }

  @SchemaMapping
  public Mono<User> manager(Guild guild, DataLoader<Integer, GuildData> loader) {
    return Mono.fromFuture(loader.load(guild.id())).map(data -> new User(data.guild().getManager()));
  }

  @SchemaMapping
  public Mono<Address> address(Guild guild, DataLoader<Integer, GuildData> loader) {
    return Mono.fromFuture(loader.load(guild.id())).map(data -> new Address(
        data.guild().getLocation().getAddr1(),
        data.guild().getLocation().getAddr2(),
        data.guild().getLocation().getPostalcode(),
        data.guild().getLocation().getCity(),
        data.guild().getLocation().getStateorprovince(),
        data.guild().getLocation().getCountry()));
  }

  @SchemaMapping
  public Mono<List<GuildMember>> members(Guild guild, DataLoader<Integer, GuildMembersData> loader) {
    return Mono.fromFuture(loader.load(guild.id()))
        .map(data -> data.members().stream().map(member -> new GuildMember(new User(member.getName()), member.getDate()))
            .collect(Collectors.toList()));
  }

}
