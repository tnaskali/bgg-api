package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class BggGeekplayV3RequestBody {

  @NotNull
  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Required to have data returned as JSON instead of HTML.
          """)
  private Integer ajax;

  @NotNull
  @Pattern(regexp = "^(save|delete)$")
  @Parameter(
      example = "save",
      description =
          """
          Action to perform.
          <p>
          Possible values are:
          <li/>save (create or update)
          <li/>delete (delete)
          """)
  private String action;

  @Min(1)
  @Parameter(
      description =
          """
          Id of the play to update or delete.
          """)
  private Integer playid;

  @Min(1)
  @Max(1)
  @Parameter(
      description =
          """
          Set to 1 during a deletion to bypass validation.
          """)
  private Integer finalize;

  private LocalDate playdate;

  private String comments;

  @Min(0)
  private Integer length;

  private String location;

  private String objecttype;

  @Min(1)
  private Integer objectid;

  @Min(0)
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

    private Integer rating;

    private Boolean win;

    @JsonProperty(value = "new")
    private Boolean _new;
  }
}
