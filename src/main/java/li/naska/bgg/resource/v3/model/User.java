package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.RankType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class User {
  private Integer id;
  private String username;
  private String firstname;
  private String lastname;
  private String avatarlink;
  private Integer yearregistered;
  private LocalDate lastlogin;
  private String stateorprovince;
  private String country;
  private String webaddress;
  private String xboxaccount;
  private String wiiaccount;
  private String psnaccount;
  private String battlenetaccount;
  private String steamaccount;
  private Integer marketrating;
  private Integer traderating;
  private Integer numbuddies;
  // paged
  private List<UserBuddy> buddies;
  private Integer numguilds;
  // paged
  private List<UserGuild> guilds;
  private UserRanking top;
  private UserRanking hot;

  @Data
  public static class UserBuddy {
    private Integer id;
    private String name;
  }

  @Data
  public static class UserGuild {
    private Integer id;
    private String name;
  }

  @Data
  public static class UserRanking {
    private List<UserRankingItem> items;
    private String domain;

    @Data
    public static class UserRankingItem {
      private String rank;
      private RankType type;
      private String id;
      private String name;
    }
  }
}
