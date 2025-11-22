package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BggThreadV1QueryParams {

  @Parameter(example = "zefquaavius", description = """
          Username to filter for.
          <p>
          <i>Syntax</i> : /thread/{id}?comments={username}
          <p>
          <i>Example</i> : /thread/381021?username=zefquaavius
          """)
  private String username;

  @Min(0)
  @Parameter(description = """
          Start article (default = 0).
          <p>
          <i>Syntax</i> : /thread/{id}?start={start}
          <p>
          <i>Example</i> : /thread/381021?start=10
          """, schema = @Schema(defaultValue = "0"))
  private Integer start;

  @Min(1)
  @Max(100)
  @Parameter(description = """
          Number of articles (default and max = 100).
          <p>
          <i>Syntax</i> : /thread/{id}?count={count}
          <p>
          <i>Example</i> : /thread/381021?count=10
          """, schema = @Schema(defaultValue = "100"))
  private Integer count;
}
