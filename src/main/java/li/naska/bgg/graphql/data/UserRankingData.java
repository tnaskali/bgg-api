package li.naska.bgg.graphql.data;

import com.boardgamegeek.user.Ranking;

public record UserRankingData(Ranking ranking) {

  public record UserRankingKey(String username, String type, String domain) {
  }

}
