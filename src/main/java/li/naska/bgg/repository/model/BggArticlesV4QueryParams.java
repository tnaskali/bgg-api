package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BggArticlesV4QueryParams {

  @NotNull
  @Min(1)
  @Parameter(
      example = "12345",
      description = """
          Thread id.
          <p>
          <i>Syntax</i> : /articles?threadid={threadid}
          <p>
          <i>Example</i> : /articles?threadid=12345
          """
  )
  private Integer threadid;

  @Min(1)
  @Parameter(
      description = """
          Page number. Page size is 25.
          <p>
          <i>Syntax</i> : /articles?threadid={threadid}&pageid={pageid}
          <p>
          <i>Example</i> : /articles?threadid=12345&pageid=2
          """,
      schema = @Schema(defaultValue = "1")
  )
  private Integer pageid;

}
