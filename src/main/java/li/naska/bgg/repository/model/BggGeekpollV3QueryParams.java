package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggGeekpollV3QueryParams {

  @NotNull
  @Pattern(regexp = "^(results)$")
  @Parameter(
      example = "results",
      description = """
          Action to perform.
          <p>
          Possible values are:
          <li/>results
          """
  )
  private String action;

  @NotNull
  @Min(1)
  @Parameter(
      example = "1589097",
      description = """
          Poll Id.
          """
  )
  private Integer pollid;

}
