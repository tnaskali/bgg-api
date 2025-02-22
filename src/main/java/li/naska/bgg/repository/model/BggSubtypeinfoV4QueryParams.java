package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BggSubtypeinfoV4QueryParams {

  @NotNull
  // @Pattern(regexp = ...)
  @Parameter(
      example = "boardgameintegration",
      description =
          """
          The object type.
          <p>
          <i>Syntax</i> : /subtypeinfo?subtype={type}
          <p>
          <i>Example</i> : /subtypeinfo?subtype=boardgameintegration
          """)
  private String subtype;

  @Deprecated
  @Min(1)
  @Parameter(
      description =
          """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful.
          <p>
          Seems to be present with a value of "1" in all requests.
          """)
  private Integer nosession;
}
