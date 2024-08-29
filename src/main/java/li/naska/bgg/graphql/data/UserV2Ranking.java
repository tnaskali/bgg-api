package li.naska.bgg.graphql.data;

import com.boardgamegeek.user.v2.Ranking;

public record UserV2Ranking(Ranking ranking) {

  public record UserRankingKey(String username, String type, String domain) {}
}
