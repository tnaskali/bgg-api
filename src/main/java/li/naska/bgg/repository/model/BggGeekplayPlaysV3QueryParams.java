package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggGeekplayPlaysV3QueryParams {

  @NotNull
  @Pattern(regexp = "^(getplays)$")
  @Parameter(
      example = "getplays",
      description = """
          Action to perform.
          <p>
          Possible values are:
          <li/>getplays
          """
  )
  private String action;

  @Parameter(
      example = "825923",
      description = """
          User id.
          """
  )
  private Integer userid;

  @Parameter(
      example = "205637",
      description = """
          Object id.
          """
  )
  private Integer objectid;

  @Pattern(regexp = "^(thing|family)$")
  @Parameter(
      example = "thing",
      description = """
          Object type.
          """
  )
  private String objecttype;

  @Deprecated
  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful.
          <p>
          Seems to be present with a value of "1" in all requests.
          """
  )
  private Integer ajax;

}
