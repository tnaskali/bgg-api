package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggThreadsV4QueryParams {

  @NotNull
  @Pattern(regexp = "^listing$")
  @Parameter(
      example = "listing",
      description =
          """
          Partial results option.
          <p>Valid options are:
          <li/>listing
          <p>
          <i>Syntax</i> : /threads?partial={partial}
          <p>
          <i>Example</i> : /threads?partial=listing
          """)
  private String partial;

  @NotNull
  @Pattern(regexp = "^(?:regions|things)$")
  @Parameter(
      example = "regions",
      description =
          """
          Associated item type.
          <p>Valid options are:
          <li/>regions
          <li/>things
          <p>
          <i>Syntax</i> : /threads?partial={partial}&assocItemType={assocItemType}&assocItemId={assocItemId}
          <p>
          <i>Example</i> : /threads?partial=listing&assocItemType=regions&assocItemId=1
          """)
  private String assocItemType;

  @NotNull
  @Min(1)
  @Parameter(
      example = "1",
      description =
          """
          Associated item id.
          <p>
          <i>Syntax</i> : /threads?partial={partial}&assocItemType={assocItemType}&assocItemId={assocItemId}
          <p>
          <i>Example</i> : /threads?partial=listing&assocItemType=regions&assocItemId=1
          """)
  private Integer assocItemId;

  @Pattern(regexp = "^(?:active|hot|recent)$")
  @Parameter(
      description =
          """
          Sort order.
          <p>Valid sort orders are:
          <li/>active (default)
          <li/>hot
          <li/>recent
          <p>
          <i>Syntax</i> : /threads?partial={partial}&assocItemType={assocItemType}&assocItemId={assocItemId}&sort={sort}
          <p>
          <i>Example</i> : /threads?partial=listing&assocItemType=regions&assocItemId=1&sort=active
          """,
      schema = @Schema(defaultValue = "active"))
  private String sort;

  @Pattern(regexp = "^(?:alltime|today|twodays|last7|last30|year)$")
  @Parameter(
      description =
          """
          Hot scope interval. Only when sort=hot.
          <p>Valid intervals are:
          <li/>alltime
          <li/>today
          <li/>twodays (default)
          <li/>last7
          <li/>last30
          <li/>year
          <p>
          <i>Syntax</i> : /threads?partial={partial}&assocItemType={assocItemType}&assocItemId={assocItemId}&sort=hot&interval={interval}
          <p>
          <i>Example</i> : /threads?partial=listing&assocItemType=regions&assocItemId=1&sort=hot&interval=twodays
          """,
      schema = @Schema(defaultValue = "twodays"))
  private String interval;

  @Min(1)
  @Parameter(
      description =
          """
          Page size.
          <p>
          <i>Syntax</i> : /threads?partial={partial}&assocItemType={assocItemType}&assocItemId={assocItemId}&perPage={count}
          <p>
          <i>Example</i> : /threads?partial=listing&assocItemType=regions&assocItemId=1&perPage=1
          """,
      schema = @Schema(defaultValue = "5"))
  private Integer perPage;

  @Min(1)
  @Parameter(
      description =
          """
          Page number. Page size is 25.
          <p>
          <i>Syntax</i> : /threads?partial={partial}&assocItemType={assocItemType}&assocItemId={assocItemId}&pageid={pageid}
          <p>
          <i>Example</i> : /threads?partial=listing&assocItemType=regions&assocItemId=1&pageid=2
          """,
      schema = @Schema(defaultValue = "1"))
  private Integer pageid;
}
