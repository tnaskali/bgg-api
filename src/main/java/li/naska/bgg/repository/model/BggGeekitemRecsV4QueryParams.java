package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggGeekitemRecsV4QueryParams {

  @NotNull
  @Min(1)
  @Parameter(
      example = "205637",
      description =
          """
          The object id.
          <p>
          <i>Syntax</i> : /linkeditems?objectid={id}&objecttype={type}&linkdata_index={linktype}
          <p>
          <i>Example</i> : /linkeditems?objectid=205637&objecttype=thing&linkdata_index=reimplementation
          """)
  private Integer objectid;

  @NotNull
  @Pattern(
      regexp = "^(company|component|event|family|media|person|property|thing|version|weblink)$")
  @Parameter(
      example = "thing",
      description =
          """
          The object type.
          <p>
          <i>Syntax</i> : /linkeditems?objectid={id}&objecttype={type}&linkdata_index={linktype}
          <p>
          <i>Example</i> : /linkeditems?objectid=205637&objecttype=thing&linkdata_index=reimplementation
          """)
  private String objecttype;

  @Min(1)
  @Parameter(
      description =
          """
          Page number for paged results.
          <p>
          <i>Syntax</i> : /linkeditems?objectid={id}&objecttype={type}&linkdata_index={linktype}&pageid={page}&showcount={count}
          <p>
          <i>Example</i> : /linkeditems?objectid=205637&objecttype=thing&linkdata_index=reimplementation&pageid=2&showcount=10
          """,
      schema = @Schema(defaultValue = "1"))
  private Integer pageid;

  @Min(1)
  @Parameter(
      description =
          """
          Page size for paged results.
          <p>
          <i>Syntax</i> : /linkeditems?objectid={id}&objecttype={type}&linkdata_index={linktype}&pageid={page}&showcount={count}
          <p>
          <i>Example</i> : /linkeditems?objectid=205637&objecttype=thing&linkdata_index=reimplementation&pageid=2&showcount=10
          """,
      schema = @Schema(defaultValue = "10"))
  private Integer showcount;
}
