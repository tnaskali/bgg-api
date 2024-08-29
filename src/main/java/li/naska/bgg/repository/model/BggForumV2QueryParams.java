package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BggForumV2QueryParams {

  @NotNull
  @Min(1)
  @Parameter(
      example = "2003721",
      description =
          """
          Specifies the id of the forum. This is the id that appears in the address of the page when visiting a forum
          in the browser.
          <p>
          <i>Syntax</i> : /forum?id={id}
          <p>
          <i>Example</i> : /forum?id=2003721
          """)
  private Integer id;

  @Min(1)
  @Parameter(
      example = "1",
      description =
          """
          The page of the thread list to return; page size is 50. Threads in the thread list are sorted in order of
          most recent post.
          <p>
          <i>Syntax</i> : /forum?id={id}&page={page}
          <p>
          <i>Example</i> : /forum?id=2003721&page=1
          """)
  private Integer page;
}
