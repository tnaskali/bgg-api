package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class BggUsersV4ResponseBody {

  private String type;

  private Integer id;

  private Integer userid;

  private String username;

  private String href;

  private String firstname;

  private String lastname;

  private String city;

  private String state;

  private String country;

  private String isocountry;

  private LocalDate regdate;

  private Integer designerid;

  private Integer publisherid;

  private Boolean hideSupporter;

  /**
   * Can be an empty array (username=zefquaavius) or a dict (username=Jester)
   */
  private AdminBadges adminBadges;

  private List<MicroBadge> userMicrobadges;

  private List<Integer> supportYears;

  private Boolean hideName;

  private List<Link> links;

  private CreditBadges creditBadges;

  private Flag flag;

  private Avatar avatar;

  private BadgeUrls badgeUrls;

  @Data
  public static class AdminBadges {

    private Boolean boardgame;

    private Boolean rpg;

    private Boolean videogame;

    private Boolean puzzle;
  }

  @Data
  public static class MicroBadge {

    private Integer slot;

    private Integer badgeid;

    private String userRedirect;
  }

  @Data
  public static class Link {

    private String rel;

    private String uri;
  }

  @Data
  public static class CreditBadges {

    private List<CreditBadge> boardgame;

    private List<CreditBadge> rpg;

    private List<CreditBadge> videogame;

    private List<CreditBadge> puzzle;
  }

  @Data
  public static class CreditBadge {

    private String badge;

    private String label;

    private String href;
  }

  @Data
  public static class Flag {

    private String src;

    private String url;
  }

  @Data
  public static class Avatar {

    private AvatarUrls urls;

    private Integer height;

    private Integer width;
  }

  @Data
  public static class AvatarUrls {

    private String mds;

    @JsonProperty(value = "mds@2x")
    private String mds_at_2x;

    private String md;

    private String sm;

    @JsonProperty(value = "default")
    private String _default;
  }

  @Data
  public static class BadgeUrls {

    @JsonProperty(value = "default")
    private String _default;

    @JsonProperty(value = "default@2x")
    private String default_at_2x;

    @JsonProperty(value = "default@3x")
    private String default_at_3x;
  }
}
