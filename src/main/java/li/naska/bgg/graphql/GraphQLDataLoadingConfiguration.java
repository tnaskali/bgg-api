package li.naska.bgg.graphql;

import com.boardgamegeek.enums.UserDomainType;
import li.naska.bgg.graphql.data.*;
import li.naska.bgg.graphql.service.GraphQLGuildsService;
import li.naska.bgg.graphql.service.GraphQLUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Configuration
public class GraphQLDataLoadingConfiguration {

  @Autowired
  private GraphQLUsersService usersService;

  @Autowired
  private GraphQLGuildsService guildsService;

  public GraphQLDataLoadingConfiguration(BatchLoaderRegistry registry) {
    registry.forTypePair(String.class, UserData.class).registerMappedBatchLoader((usernames, env) ->
        Flux.fromIterable(usernames)
            .flatMap(username -> Mono.just(username).zipWith(usersService.getUser(username)))
            .collectMap(Tuple2::getT1, tuple -> new UserData(tuple.getT2()))
    );
    registry.forTypePair(String.class, UserGuildsData.class).registerMappedBatchLoader((usernames, env) ->
        Flux.fromIterable(usernames)
            .flatMap(username -> Mono.just(username).zipWith(usersService.getUserGuilds(username)))
            .collectMap(Tuple2::getT1, tuple -> new UserGuildsData(tuple.getT2()))
    );
    registry.forTypePair(String.class, UserBuddiesData.class).registerMappedBatchLoader((usernames, env) ->
        Flux.fromIterable(usernames)
            .flatMap(username -> Mono.just(username).zipWith(usersService.getUserBuddies(username)))
            .collectMap(Tuple2::getT1, tuple -> new UserBuddiesData(tuple.getT2()))
    );
    registry.forTypePair(UserRankingData.UserRankingKey.class, UserRankingData.class).registerMappedBatchLoader((keys, env) ->
        Flux.fromIterable(keys)
            .flatMap(key -> Mono.just(key).zipWith(usersService.getUserRanking(key.username(), key.type(), UserDomainType.fromValue(key.domain()))))
            .collectMap(Tuple2::getT1, tuple -> new UserRankingData(tuple.getT2()))
    );
    registry.forTypePair(Integer.class, GuildData.class).registerMappedBatchLoader((ids, env) ->
        Flux.fromIterable(ids)
            .flatMap(id -> Mono.just(id).zipWith(guildsService.getGuild(id)))
            .collectMap(Tuple2::getT1, tuple -> new GuildData(tuple.getT2()))
    );
    registry.forTypePair(Integer.class, GuildMembersData.class).registerMappedBatchLoader((ids, env) ->
        Flux.fromIterable(ids)
            .flatMap(id -> Mono.just(id).zipWith(guildsService.getMembers(id)))
            .collectMap(Tuple2::getT1, tuple -> new GuildMembersData(tuple.getT2()))
    );
  }

}

