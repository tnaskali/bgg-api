package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BggListitemsV4QueryParams {

  @NotNull
  @Min(1)
  @Parameter(
      example = "250030",
      description = """
          The geeklist id.
          <p>
          <i>Syntax</i> : /listitems?listid={listid}
          <p>
          <i>Example</i> : /listitems?listid=250030
          """
  )
  private Integer listid;

  @Min(1)
  @Parameter(
      description = """
          The page number. Page size is 25.
          <p>
          <i>Syntax</i> : /listitems?listid={listid}&page={count}
          <p>
          <i>Example</i> : /listitems?listid=250030&page=2
          """,
      schema = @Schema(defaultValue = "1")
  )
  private Integer page;

}
