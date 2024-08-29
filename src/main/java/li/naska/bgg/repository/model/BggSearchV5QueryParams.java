package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BggSearchV5QueryParams {

  @NotNull
  @Parameter(
      example = "corona",
      description =
          """
          Search string.
          <p>
          <i>Syntax</i> : /search?q={query}
          <p>
          <i>Example</i> : /search?q=corona
          """)
  private String q;

  @NotNull
  @Min(1)
  @Max(100)
  @Parameter(
      example = "20",
      description =
          """
          Limits the number of results.
          <p>
          <i>Syntax</i> : /search?q={query}&showcount={count}
          <p>
          <i>Example</i> : /search?q=corona&showcount=20
          """)
  private Integer showcount;

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
