package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BggGeeklistReactionsV4QueryParams {

  @Min(1)
  @Parameter(
      description = """
          Page id. Page size is 20.
          <p>
          <i>Syntax</i> : /geeklists/{id}/reactions?pageid={page}
          <p>
          <i>Example</i> : /geeklists/250030/reactions?pageid=2
          """,
      schema = @Schema(defaultValue = "1")
  )
  private Integer pageid;

  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          Shows only reactions total count.
          <p>
          <i>Syntax</i> : /geeklists/{id}/reactions?totalonly=1
          <p>
          <i>Example</i> : /geeklists/250030/reactions?totalonly=1
          """
  )
  private Integer totalonly;

}
