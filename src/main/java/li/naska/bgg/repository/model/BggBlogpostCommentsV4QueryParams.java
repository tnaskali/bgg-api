package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggBlogpostCommentsV4QueryParams {

  @Min(0)
  @Parameter(
      example = "8494604",
      description =
          """
          Paging cursor. Page is calculated from there.
          <p>
          <i>Syntax</i> : /blogposts/{id}/comments?commentid={cursor}
          <p>
          <i>Example</i> : /blogposts/166224/comments?commentid=5782800
          """)
  private Integer commentid;

  @Min(1)
  @Parameter(
      example = "1",
      description =
          """
          Page size. Default is all comments.
          <p>
          <i>Syntax</i> : /blogposts/{id}/comments?perPage={count}
          <p>
          <i>Example</i> : /blogposts/166224/comments?perPage=1
          """)
  private Integer perPage;

  @Pattern(regexp = "^backward$")
  @Parameter(
      example = "backward",
      description =
          """
          Changes paging direction (forward by default).
          <p>
          <i>Syntax</i> : /blogposts/{id}/comments?commentid={cursor}
          <p>
          <i>Example</i> : /blogposts/166224/comments?commentid=5782800&direction=backward
          """)
  private String direction;

  @Min(1)
  @Max(1)
  @Parameter(
      description =
          """
          Shows only comments total count.
          <p>
          <i>Syntax</i> : /blogposts/{id}/comments?totalonly=1
          <p>
          <i>Example</i> : /blogposts/166224/comments?totalonly=1
          """)
  private Integer totalonly;

  @Min(1)
  @Max(1)
  @Parameter(
      description =
          """
          Shows only comments metadata.
          <p>
          <i>Syntax</i> : /blogposts/{id}/comments?metaonly=1
          <p>
          <i>Example</i> : /blogposts/166224/comments?metaonly=1
          """)
  private Integer metaonly;
}
