package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.RankedItemType;
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
  private List<Buddy> buddies;
  private Integer numguilds;
  // paged
  private List<Guild> guilds;
  private Ranking top;
  private Ranking hot;

  @Data
  public static class Buddy {
    private Integer id;
    private String name;
  }

  @Data
  public static class Ranking {
    private List<RankedItem> items;
    private String domain;

    @Data
    public static class RankedItem {
      private String rank;
      private RankedItemType type;
      private String id;
      private String name;
    }
  }
}
