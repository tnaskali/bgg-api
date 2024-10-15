package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggBlogpostsV4QueryParams {

  @Pattern(regexp = "^(hot|recent)$")
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
          <i>Example</i> : /blogs/posts?sort=recent
          """,
      schema = @Schema(defaultValue = "recent"))
  private String sort;

  @Pattern(regexp = "^(alltime)$")
  @Parameter(
      description =
          """
          Interval.
          <p>Valid intervals are:
          <li/>alltime
          <p>
          <i>Syntax</i> : /blogs/posts?interval={interval}
          <p>
          <i>Example</i> : /blogs/posts?interval=alltime
          """)
  private String interval;
}
