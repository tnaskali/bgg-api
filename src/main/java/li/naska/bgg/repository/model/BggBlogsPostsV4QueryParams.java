package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggBlogsPostsV4QueryParams {

  @Min(1)
  @Parameter(
      example = "20963",
      description =
          """
          The object id.
          <p>
          <i>Syntax</i> : /blogs/posts?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /blogs/posts?objectid=20963&objecttype=thing
          """)
  private Integer objectid;

  @Pattern(
      regexp = "^(?:company|component|event|family|media|person|property|thing|version|weblink)$")
  @Parameter(
      example = "thing",
      description =
          """
          The object type.
          <p>
          <i>Syntax</i> : /blogs/posts?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /blogs/posts?objectid=20963&objecttype=thing
          """)
  private String objecttype;

  @Min(1)
  @Parameter(
      description =
          """
          Blog id.
          <p>
          <i>Syntax</i> : /blogs/posts?blogid={blogid}
          <p>
          <i>Example</i> : /blogs/posts?blogid=9268
          """,
      schema = @Schema(defaultValue = "9268"))
  private Integer blogid;

  @Min(1)
  @Max(1)
  @Parameter(
      description =
          """
          Filter enabled.
          <p>
          <i>Syntax</i> : /blogs/posts?objectid={id}&objecttype={type}&filterblogs={filterblogs}
          <p>
          <i>Example</i> : /blogs/posts?objectid=20963&objecttype=thing&filterblogs=1
          """,
      schema = @Schema(defaultValue = "1"))
  private Integer filterblogs;

  @Min(1)
  @Parameter(
      description =
          """
          Page number for paged results.
          <p>
          <i>Syntax</i> : /blogs/posts?objectid={id}&objecttype={type}&pageid={page}
          <p>
          <i>Example</i> : /blogs/posts?objectid=20963&objecttype=thing&pageid=2
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
          <i>Syntax</i> : /blogs/posts?objectid={id}&objecttype={type}&showcount={count}
          <p>
          <i>Example</i> : /blogs/posts?objectid=20963&objecttype=thing&showcount=10
          """,
      schema = @Schema(defaultValue = "10"))
  private Integer showcount;

  @Pattern(regexp = "^(?:hot|recent)$")
  @Parameter(
      description =
          """
          Sort order.
          <p>Valid sort orders are:
          <li/>hot
          <li/>recent
          <p>
          <i>Syntax</i> : /blogs/posts?objectid={id}&objecttype={type}&sort={sort}
          <p>
          <i>Example</i> : /blogs/posts?objectid=20963&objecttype=thing&sort=recent
          """,
      schema = @Schema(defaultValue = "recent"))
  private String sort;

  @Pattern(regexp = "^(?:alltime|today|twodays|last7|last30|year)$")
  @Parameter(
      description =
          """
          Hot scope interval.
          <p>Valid intervals are:
          <li/>alltime (default)
          <li/>today
          <li/>twodays
          <li/>last7
          <li/>last30
          <li/>year
          <p>
          <i>Syntax</i> : /blogs/posts?objectid={id}&objecttype={type}&interval={interval}
          <p>
          <i>Example</i> : /blogs/posts?objectid=20963&objecttype=thing&interval=alltime
          """,
      schema = @Schema(defaultValue = "alltime"))
  private String interval;

  @Deprecated
  @Min(1)
  @Max(1)
  @Parameter(
      description =
          """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful.
          <p>
          Seems to be present with a value of "1" in all requests.
          """)
  private Integer ajax;
}
