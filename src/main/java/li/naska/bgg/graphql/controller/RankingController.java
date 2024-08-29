package li.naska.bgg.graphql.controller;

import java.util.List;
import java.util.stream.Collectors;
import li.naska.bgg.graphql.data.UserV2Ranking;
import li.naska.bgg.graphql.model.RankedItem;
import li.naska.bgg.graphql.model.Ranking;
import li.naska.bgg.graphql.model.enums.Domain;
import li.naska.bgg.graphql.service.GraphQLUsersService;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Controller("GraphQLRankingController")
public class RankingController {

  public RankingController(BatchLoaderRegistry registry, GraphQLUsersService usersService) {
    registry
        .forTypePair(UserV2Ranking.UserRankingKey.class, UserV2Ranking.class)
        .registerMappedBatchLoader(
            (keys, env) ->
                Flux.fromIterable(keys)
                    .flatMap(
                        key ->
                            Mono.just(key)
                                .zipWith(
                                    usersService.getUserRanking(
                                        key.username(), key.type(), Domain.valueOf(key.domain()))))
                    .collectMap(Tuple2::getT1, tuple -> new UserV2Ranking(tuple.getT2())));
  }

  @SchemaMapping
  public Mono<List<RankedItem>> boardgame(
      Ranking ranking, DataLoader<UserV2Ranking.UserRankingKey, UserV2Ranking> loader) {
    return Mono.fromFuture(
            loader.load(
                new UserV2Ranking.UserRankingKey(
                    ranking.user().username(), ranking.type(), "boardgame")))
        .map(
            data ->
                data.ranking().getItems().stream()
                    .map(
                        item ->
                            new RankedItem(
                                item.getRank(), item.getType(), item.getId(), item.getName()))
                    .collect(Collectors.toList()));
  }

  @SchemaMapping
  public Mono<List<RankedItem>> rpg(
      Ranking ranking, DataLoader<UserV2Ranking.UserRankingKey, UserV2Ranking> loader) {
    return Mono.fromFuture(
            loader.load(
                new UserV2Ranking.UserRankingKey(ranking.user().username(), ranking.type(), "rpg")))
        .map(
            data ->
                data.ranking().getItems().stream()
                    .map(
                        item ->
                            new RankedItem(
                                item.getRank(), item.getType(), item.getId(), item.getName()))
                    .collect(Collectors.toList()));
  }

  @SchemaMapping
  public Mono<List<RankedItem>> videogame(
      Ranking ranking, DataLoader<UserV2Ranking.UserRankingKey, UserV2Ranking> loader) {
    return Mono.fromFuture(
            loader.load(
                new UserV2Ranking.UserRankingKey(
                    ranking.user().username(), ranking.type(), "videogame")))
        .map(
            data ->
                data.ranking().getItems().stream()
                    .map(
                        item ->
                            new RankedItem(
                                item.getRank(), item.getType(), item.getId(), item.getName()))
                    .collect(Collectors.toList()));
  }
}
