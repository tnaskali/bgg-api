package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BggBlogpostReactionsV4QueryParams {

  @Min(1)
  @Max(1)
  @Parameter(
      description =
          """
          Shows only reactions total count.
          <p>
          <i>Syntax</i> : /blogposts/{id}/reactions?totalonly=1
          <p>
          <i>Example</i> : /blogposts/166224/reactions?totalonly=1
          """)
  private Integer totalonly;
}
