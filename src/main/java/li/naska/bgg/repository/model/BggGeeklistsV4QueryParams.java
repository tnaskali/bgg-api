package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggGeeklistsV4QueryParams {

  @NotNull
  @Min(1)
  @Parameter(
      example = "1000",
      description = """
          The object id.
          <p>
          <i>Syntax</i> : /geeklists?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /geeklists?objectid=1000&objecttype=thing
          """
  )
  private Integer objectid;

  @NotNull
  @Pattern(regexp = "^(company|component|event|family|media|person|property|thing|version|weblink)$")
  @Parameter(
      example = "thing",
      description = """
          The object type.
          <p>
          <i>Syntax</i> : /geeklists?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /geeklists?objectid=1000&objecttype=thing
          """
  )
  private String objecttype;

  @Pattern(regexp = "^(hot|recent)$")
  @Parameter(
      description = """
          Sort order.
          <p>Valid sort orders are:
          <li/>hot
          <li/>recent
          <p>
          <i>Syntax</i> : /geeklists?objectid={id}&objecttype={type}&sort={sort}
          <p>
          <i>Example</i> : /geeklists?objectid=205637&objecttype=thing&sort=recent
          """,
      schema = @Schema(defaultValue = "recent")
  )
  private String sort;

  @Min(1)
  @Parameter(
      description = """
          Page number for paged results.
          <p>
          <i>Syntax</i> : /geeklists?objectid={id}&objecttype={type}&pageid={page}
          <p>
          <i>Example</i> : /geeklists?objectid=205637&objecttype=thing&pageid=2
          """,
      schema = @Schema(defaultValue = "1")
  )
  private Integer pageid;

  @Min(1)
  @Max(50)
  @Parameter(
      example = "10",
      description = """
          Page size for paged results.
          <p>
          <i>Syntax</i> : /geeklists?objectid={id}&objecttype={type}&showcount={count}
          <p>
          <i>Example</i> : /geeklists?objectid=205637&objecttype=thing&showcount=10
          """,
      schema = @Schema(defaultValue = "1")
  )
  private Integer showcount;

  @Min(1)
  @Max(50)
  @Parameter(
      example = "3",
      description = """
          Number of items to retrieve per geeklist.
          <p>
          <i>Syntax</i> : /geeklists?objectid={id}&objecttype={type}&itemcount={count}
          <p>
          <i>Example</i> : /geeklists?objectid=205637&objecttype=thing&itemcount=3
          """,
      schema = @Schema(defaultValue = "0")
  )
  private Integer itemcount;

  @Deprecated
  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful.
          <p>
          Seems to be present with a value of "1" in all requests.
          """
  )
  private Integer ajax;

  @Deprecated
  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful.
          <p>
          Seems to be present with a value of "1" in all requests.
          """
  )
  private Integer nosession;

}
