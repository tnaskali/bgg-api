package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggGeekitemLinkeditemsV4QueryParams {

  @NotNull
  @Min(1)
  @Parameter(example = "205637", description = """
          The object id.
          <p>
          <i>Syntax</i> : /linkeditems?objectid={id}&objecttype={type}&linkdata_index={linktype}
          <p>
          <i>Example</i> : /linkeditems?objectid=205637&objecttype=thing&linkdata_index=reimplementation
          """)
  private Integer objectid;

  @NotNull
  @Pattern(
      regexp = "^(?:company|component|event|family|media|person|property|thing|version|weblink)$")
  @Parameter(example = "thing", description = """
          The object type.
          <p>
          <i>Syntax</i> : /linkeditems?objectid={id}&objecttype={type}&linkdata_index={linktype}
          <p>
          <i>Example</i> : /linkeditems?objectid=205637&objecttype=thing&linkdata_index=reimplementation
          """)
  private String objecttype;

  @NotNull
  @Parameter(example = "reimplementation", description = """
          The link type. Valid link types depends on object type and subtype.
          <p>
          <i>Syntax</i> : /linkeditems?objectid={id}&objecttype={type}&linkdata_index={linktype}
          <p>
          <i>Example</i> : /linkeditems?objectid=205637&objecttype=thing&linkdata_index=reimplementation
          """)
  private String linkdata_index;

  @Min(1)
  @Parameter(description = """
          Page number for paged results.
          <p>
          <i>Syntax</i> : /linkeditems?objectid={id}&objecttype={type}&linkdata_index={linktype}&pageid={page}
          <p>
          <i>Example</i> : /linkeditems?objectid=205637&objecttype=thing&linkdata_index=reimplementation&pageid=2
          """, schema = @Schema(defaultValue = "1"))
  private Integer pageid;

  @Min(1)
  @Parameter(description = """
          Page size for paged results.
          <p>
          <i>Syntax</i> : /linkeditems?objectid={id}&objecttype={type}&linkdata_index={linktype}&showcount={count}
          <p>
          <i>Example</i> : /linkeditems?objectid=205637&objecttype=thing&linkdata_index=reimplementation&showcount=10
          """, schema = @Schema(defaultValue = "1"))
  private Integer showcount;

  @Parameter(description = """
          Sort order. Valid sort orders depends on object type and subtype.
          <p>
          <i>Syntax</i> : /linkeditems?objectid={id}&objecttype={type}&linkdata_index={linktype}&sort={sort}
          <p>
          <i>Example</i> : /linkeditems?objectid=205637&objecttype=thing&linkdata_index=reimplementation&sort=name
          """, schema = @Schema(defaultValue = "name"))
  private String sort;

  private String subtype;

  @Deprecated
  @Min(1)
  @Max(1)
  @Parameter(description = """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful.
          <p>
          Seems to be present with a value of "1" in all requests.
          """)
  private Integer ajax;

  @Deprecated
  @Min(1)
  @Max(1)
  @Parameter(description = """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful.
          <p>
          Seems to be present with a value of "1" in all requests.
          """)
  private Integer nosession;
}
