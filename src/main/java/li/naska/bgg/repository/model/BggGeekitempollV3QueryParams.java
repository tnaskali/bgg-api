package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggGeekitempollV3QueryParams {

  @NotNull
  @Pattern(regexp = "^view$")
  @Parameter(
      example = "view",
      description =
          """
          Action to perform.
          <p>
          Possible values are:
          <li/>view
          """)
  private String action;

  @NotNull
  @Pattern(
      regexp = "^(?:boardgamesubdomain|boardgameweight|languagedependence|numplayers|playerage)$")
  @Parameter(
      example = "boardgamesubdomain",
      description =
          """
          Poll type.
          <p>
          Possible values are:
          <li/>boardgamesubdomain
          <li/>boardgameweight
          <li/>languagedependence
          <li/>numplayers
          <li/>playerage
          """)
  private String itempolltype;

  @NotNull
  @Min(1)
  @Parameter(example = "366013", description = """
          Object Id.
          """)
  private Integer objectid;

  @NotNull
  @Pattern(regexp = "^thing$")
  @Parameter(example = "thing", description = """
          Object type.
          """)
  private String objecttype;
}
