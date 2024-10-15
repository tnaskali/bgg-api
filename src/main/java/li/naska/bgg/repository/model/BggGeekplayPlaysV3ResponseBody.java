package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class BggGeekplayPlaysV3ResponseBody {
  private List<Play> plays;
  private Integer eventcount;
  private Integer userid;
  private String objecttype;
  private Integer objectid;

  @Data
  public static class Play {
    private Integer playid;
    private Integer userid;
    private String objecttype;
    private Integer objectid;
    private LocalDateTime tstamp;
    private LocalDate playdate;
    private Integer quantity;
    private Integer length;
    private String location;
    private Comments comments;
    private Integer incomplete;
    private Integer nowinstats;
    private String winstate;
    private Integer online;
    private Integer length_ms;
    private String name;
    private Integer numplayers;
    private List<Player> players;
    private List<Subtype> subtypes;
  }

  @Data
  public static class Comments {
    private String value;
    private String rendered;
  }

  @Data
  public static class Player {
    private Integer playerid;
    private Integer playid;
    private String position;
    private String color;
    private String score;
    private Integer win;

    @JsonProperty(value = "new")
    private Integer _new;

    private Integer rating;
    private Integer uplayerid;
    private String name;
    private Integer userid;
    private String username;
  }

  @Data
  public static class Subtype {
    private String subtype;
  }
}
