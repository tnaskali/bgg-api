package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggListitemCommentsV4QueryParams {

  @Min(1)
  @Parameter(
      example = "1",
      description =
          """
          Page size. Default is all comments.
          <p>
          <i>Syntax</i> : /listitems/{id}/comments?perPage={count}
          <p>
          <i>Example</i> : /listitems/6632367/comments?perPage=1
          """)
  private Integer perPage;

  @Min(0)
  @Parameter(
      example = "8494604",
      description =
          """
          Paging cursor. Page is calculated from there.
          <p>
          <i>Syntax</i> : /listitems/{id}/comments?commentid={cursor}
          <p>
          <i>Example</i> : /listitems/6632367/comments?commentid=8603052
          """)
  private Integer commentid;

  @Pattern(regexp = "^(backward)$")
  @Parameter(
      example = "backward",
      description =
          """
          Changes paging direction (forward by default).
          <p>
          <i>Syntax</i> : /listitems/{id}/comments?commentid={cursor}
          <p>
          <i>Example</i> : /listitems/6632367/comments?commentid=8603052&direction=backward
          """)
  private String direction;

  @Min(1)
  @Max(1)
  @Parameter(
      description =
          """
          Shows only comments total count.
          <p>
          <i>Syntax</i> : /listitems/{id}/comments?totalonly=1
          <p>
          <i>Example</i> : /listitems/6632367/comments?totalonly=1
          """)
  private Integer totalonly;

  @Min(1)
  @Max(1)
  @Parameter(
      description =
          """
          Shows only comments metadata.
          <p>
          <i>Syntax</i> : /listitems/{id}/comments?metaonly=1
          <p>
          <i>Example</i> : /listitems/6632367/comments?metaonly=1
          """)
  private Integer metaonly;
}
