package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ItemType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Data
public class BggGeekplayRequestBody {

  // technical properties

  @NotNull
  @Min(1)
  @Max(1)
  private Integer ajax;
  @NotNull
  @Pattern(regexp = "^(save|delete)$")
  private String action;
  @Min(1)
  @Max(1)
  private Integer finalize;
  @Min(2)
  @Max(2)
  private Integer version;

  // business properties

  private Integer playid;
  private LocalDate playdate;
  private String comments;
  private Integer length;
  private String location;
  private ItemType objecttype;
  private Integer objectid;
  private Integer quantity;
  private Boolean twitter;
  private String twitter_username;
  private String twitter_password;
  private Boolean incomplete;
  private Boolean nowinstats;
  private List<GeekplayPlayer> players;

  @Data
  public static class GeekplayPlayer {
    private String username;
    private String name;
    private String position;
    private String color;
    private String score;
    private String rating;
    private Boolean win;
    @JsonProperty(value = "new")
    private Boolean _new;
  }

}
