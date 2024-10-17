package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggForumsThreadsV4QueryParams {

  @NotNull
  @Min(1)
  @Parameter(
      example = "205637",
      description =
          """
          The object id.
          <p>
          <i>Syntax</i> : /forums/threads?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /forums/threads?objectid=205637&objecttype=thing
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
          <i>Syntax</i> : /forums/threads?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /forums/threads?objectid=205637&objecttype=thing
          """)
  private String objecttype;

  @Parameter(
      example = "66",
      description =
          """
          Filter by forum id. Default is 0 for all forums.
          <p>
          <i>Syntax</i> : /forums/threads?objectid={id}&objecttype={type}&forumid={forumid}
          <p>
          <i>Example</i> : /forums/threads?objectid=205637&objecttype=thing&forumid=66
          """,
      schema = @Schema(defaultValue = "0"))
  private Integer forumid;

  @Min(1)
  @Parameter(
      description =
          """
          Page number for paged results.
          <p>
          <i>Syntax</i> : /forums/threads?objectid={id}&objecttype={type}&pageid={page}
          <p>
          <i>Example</i> : /forums/threads?objectid=205637&objecttype=thing&pageid=2
          """,
      schema = @Schema(defaultValue = "1"))
  private Integer pageid;

  @Min(1)
  @Max(50)
  @Parameter(
      description =
          """
          Page size for paged results.
          <p>
          <i>Syntax</i> : /forums/threads?objectid={id}&objecttype={type}&showcount={count}
          <p>
          <i>Example</i> : /forums/threads?objectid=205637&objecttype=thing&showcount=10
          """,
      schema = @Schema(defaultValue = "1"))
  private Integer showcount;

  @Pattern(regexp = "^(hot|recent)$")
  @Parameter(
      description =
          """
          Sort order.
          <p>Valid sort orders are:
          <li/>hot
          <li/>recent
          <p>
          <i>Syntax</i> : /forums/threads?objectid={id}&objecttype={type}&sort={sort}
          <p>
          <i>Example</i> : /forums/threads?objectid=205637&objecttype=thing&sort=recent
          """,
      schema = @Schema(defaultValue = "recent"))
  private String sort;
}
