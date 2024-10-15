package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BggCommentTipsV4QueryParams {

  @Min(1)
  @Max(1)
  @Parameter(
      description =
          """
          Shows only tips total count.
          <p>
          <i>Syntax</i> : /comments/{id}/tips?totalonly=1
          <p>
          <i>Example</i> : /comments/12450439/tips?totalonly=1
          """)
  private Integer totalonly;
}
