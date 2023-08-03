package li.naska.bgg.graphql;

import li.naska.bgg.graphql.data.*;
import li.naska.bgg.graphql.model.enums.Domain;
import li.naska.bgg.graphql.service.GraphQLGuildsService;
import li.naska.bgg.graphql.service.GraphQLUsersService;
import li.naska.bgg.repository.BggMicrobadgesV4Repository;
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

  @Autowired
  private BggMicrobadgesV4Repository microbadgesService;

  public GraphQLDataLoadingConfiguration(BatchLoaderRegistry registry) {
    // User
    registry.forTypePair(Integer.class, UserV4.class).registerMappedBatchLoader((ids, env) ->
        Flux.fromIterable(ids)
            .flatMap(id -> Mono.just(id).zipWith(usersService.getUser(id)))
            .collectMap(Tuple2::getT1, tuple -> new UserV4(tuple.getT2()))
    );
    registry.forTypePair(String.class, UserV2.class).registerMappedBatchLoader((usernames, env) ->
        Flux.fromIterable(usernames)
            .flatMap(username -> Mono.just(username).zipWith(usersService.getUser(username)))
            .collectMap(Tuple2::getT1, tuple -> new UserV2(tuple.getT2()))
    );
    registry.forTypePair(String.class, UserV2Guilds.class).registerMappedBatchLoader((usernames, env) ->
        Flux.fromIterable(usernames)
            .flatMap(username -> Mono.just(username).zipWith(usersService.getUserGuilds(username)))
            .collectMap(Tuple2::getT1, tuple -> new UserV2Guilds(tuple.getT2()))
    );
    registry.forTypePair(String.class, UserV2Buddies.class).registerMappedBatchLoader((usernames, env) ->
        Flux.fromIterable(usernames)
            .flatMap(username -> Mono.just(username).zipWith(usersService.getUserBuddies(username)))
            .collectMap(Tuple2::getT1, tuple -> new UserV2Buddies(tuple.getT2()))
    );
    registry.forTypePair(UserV2Ranking.UserRankingKey.class, UserV2Ranking.class).registerMappedBatchLoader((keys, env) ->
        Flux.fromIterable(keys)
            .flatMap(key -> Mono.just(key).zipWith(usersService.getUserRanking(key.username(), key.type(), Domain.valueOf(key.domain()))))
            .collectMap(Tuple2::getT1, tuple -> new UserV2Ranking(tuple.getT2()))
    );
    // Guild
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
    // Microbadge
    registry.forTypePair(Integer.class, MicrobadgeV4.class).registerMappedBatchLoader((ids, env) ->
        Flux.fromIterable(ids)
            .flatMap(id -> Mono.just(id).zipWith(microbadgesService.getMicrobadge(id)))
            .collectMap(Tuple2::getT1, tuple -> new MicrobadgeV4(tuple.getT2()))
    );
  }

}

