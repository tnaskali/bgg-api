package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BggGeekplayV3RequestBody {

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

  // business properties

  private Integer playid;
  private LocalDate playdate;
  private String comments;
  private Integer length;
  private String location;
  private String objecttype;
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
