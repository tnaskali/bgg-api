package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BggListitemReactionsV4QueryParams {

  @Min(1)
  @Max(1)
  @Parameter(
      description =
          """
          Shows only reactions total count.
          <p>
          <i>Syntax</i> : /listitems/{id}/reactions?totalonly=1
          <p>
          <i>Example</i> : /listitems/6632367/reactions?totalonly=1
          """)
  private Integer totalonly;
}
