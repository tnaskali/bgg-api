package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BggGeeklistV1QueryParams {

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description = """
          Retrieve comments? (default absent, set to 1 if you want comments)
          <p>
          <i>Syntax</i> : /geeklist/{id}?comments=1
          <p>
          <i>Example</i> : /geeklist/11205?comments=1
          """
  )
  private Integer comments;

  @Min(1)
  @Deprecated
  private Integer start;

  @Min(1)
  @Deprecated
  private Integer count;

}
