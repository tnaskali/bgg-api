package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggBlogpostsV4QueryParams {

  @Pattern(regexp = "^(?:hot|recent)$")
  @Parameter(
      description =
          """
          Sort order.
          <p>Valid sort orders are:
          <li/>hot
          <li/>recent
          <p>
          <i>Syntax</i> : /blogposts?sort={sort}
          <p>
          <i>Example</i> : /blogposts?sort=recent
          """,
      schema = @Schema(defaultValue = "recent"))
  private String sort;

  @Pattern(regexp = "^(?:alltime|today|twodays|last7|last30|year)$")
  @Parameter(
      description =
          """
          Hot scope interval.
          <p>Valid intervals are:
          <li/>alltime (default)
          <li/>today
          <li/>twodays
          <li/>last7
          <li/>last30
          <li/>year
          <p>
          <i>Syntax</i> : /blogposts?interval={interval}
          <p>
          <i>Example</i> : /blogposts?interval=alltime
          """,
      schema = @Schema(defaultValue = "alltime"))
  private String interval;
}
