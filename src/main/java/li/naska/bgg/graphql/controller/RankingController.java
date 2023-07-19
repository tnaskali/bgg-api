package li.naska.bgg.graphql.controller;

import li.naska.bgg.graphql.data.UserRankingData;
import li.naska.bgg.graphql.model.RankedItem;
import li.naska.bgg.graphql.model.Ranking;
import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Controller("GraphQLRankingController")
public class RankingController {

  @SchemaMapping
  public Mono<List<RankedItem>> boardgame(Ranking ranking, DataLoader<UserRankingData.UserRankingKey, UserRankingData> loader) {
    return Mono.fromFuture(loader.load(new UserRankingData.UserRankingKey(ranking.user().username(), ranking.type(), "boardgame")))
        .map(data -> data.ranking().getItems().stream().map(item -> new RankedItem(
                item.getRank(),
                item.getType(),
                item.getId(),
                item.getName()))
            .collect(Collectors.toList())
        );
  }

  @SchemaMapping
  public Mono<List<RankedItem>> rpg(Ranking ranking, DataLoader<UserRankingData.UserRankingKey, UserRankingData> loader) {
    return Mono.fromFuture(loader.load(new UserRankingData.UserRankingKey(ranking.user().username(), ranking.type(), "rpg")))
        .map(data -> data.ranking().getItems().stream().map(item -> new RankedItem(
                item.getRank(),
                item.getType(),
                item.getId(),
                item.getName()))
            .collect(Collectors.toList())
        );
  }

  @SchemaMapping
  public Mono<List<RankedItem>> videogame(Ranking ranking, DataLoader<UserRankingData.UserRankingKey, UserRankingData> loader) {
    return Mono.fromFuture(loader.load(new UserRankingData.UserRankingKey(ranking.user().username(), ranking.type(), "videogame")))
        .map(data -> data.ranking().getItems().stream().map(item -> new RankedItem(
                item.getRank(),
                item.getType(),
                item.getId(),
                item.getName()))
            .collect(Collectors.toList())
        );
  }

}
