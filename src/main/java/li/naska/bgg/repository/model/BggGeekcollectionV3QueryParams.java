package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggGeekcollectionV3QueryParams {

  @NotNull
  @Pattern(regexp = "^exportcsv$")
  @Parameter(
      example = "exportcsv",
      description =
          """
          The action to perform.
          <p>
          Possible values are:
          <li/>exportcsv
          """)
  private String action;

  @NotNull
  @Pattern(regexp = "^(?:boardgame|rpg|videogame)$")
  @Parameter(
      example = "boardgame",
      description =
          """
          Type (domain) of collection.
          <p>
          Possible values are:
          <li/>boardgame
          <li/>rpg
          <li/>videogame
          """)
  private String subtype;

  @NotNull
  @Parameter(
      example = "Hilaryg",
      description = """
          Type username whose collection to export.
          """)
  private String username;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Set to 1 to retrieve all items, by default only items marked as owned will be retrieved.
          """)
  private Integer all;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Useful to have minimal HTML returned in the processing response.
          """)
  private Integer ajax;

  @Deprecated
  @Pattern(regexp = "^csv$")
  @Parameter(
      description =
          """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful.
          """)
  private String exporttype;
}
