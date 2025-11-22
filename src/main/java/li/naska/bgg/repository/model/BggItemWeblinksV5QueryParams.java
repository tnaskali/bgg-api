package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggItemWeblinksV5QueryParams {

  @NotNull
  @Min(1)
  @Parameter(example = "261980", description = """
          The object id.
          <p>
          <i>Syntax</i> : /geeklists?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /geeklists?objectid=261980&objecttype=thing
          """)
  private Integer objectid;

  @NotNull
  @Pattern(
      regexp = "^(?:company|component|event|family|media|person|property|thing|version|weblink)$")
  @Parameter(example = "thing", description = """
          The object type.
          <p>
          <i>Syntax</i> : /geeklists?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /geeklists?objectid=261980&objecttype=thing
          """)
  private String objecttype;

  @NotNull
  @Min(1)
  @Max(50)
  @Parameter(example = "10", description = """
          Page size for paged results.
          <p>
          <i>Syntax</i> : /geeklists?objectid={id}&objecttype={type}&showcount={count}
          <p>
          <i>Example</i> : /geeklists?objectid=261980&objecttype=thing&showcount=10
          """)
  private Integer showcount;

  @Min(1)
  @Parameter(description = """
          Page number for paged results.
          <p>
          <i>Syntax</i> : /geeklists?objectid={id}&objecttype={type}&showcount=10&pageid={page}
          <p>
          <i>Example</i> : /geeklists?objectid=261980&objecttype=thing&showcount=10&pageid=2
          """, schema = @Schema(defaultValue = "1"))
  private Integer pageid;

  @Parameter(
      example = "%7B%22languagefilter%22:2184,%22categoryfilter%22:2708%7D",
      description = """
          Filter to apply to the results. Can filter by language or category.
          <p>
          <i>Syntax</i> : /geeklists?objectid={id}&objecttype={type}&showcount={page}&filter=%7B%22languagefilter%22:{languageid},%22categoryfilter%22:{categoryid}%7D
          <i>Syntax</i> : /geeklists?objectid={id}&objecttype={type}&showcount={page}&filter={"languagefilter":{languageid},"categoryfilter":{categoryid}}
          <p>
          <i>Example</i> : /geeklists?objectid=261980&objecttype=thing&showcount=10
          <i>Example</i> : /geeklists?objectid=261980&objecttype=thing&showcount=10&filter={"languagefilter":2184,"categoryfilter":2708}

          """)
  private String filter;

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
  @Parameter(description = """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful
          <p>
          Seems to be present with a value of "" (no value) in all requests.
          """)
  private String domain;

  @Deprecated
  @Min(1)
  @Max(1)
  @Parameter(description = """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful
          <p>
          Seems to be present with a value of "1" in all requests.
          """)
  private Integer totalonly;

  @Deprecated
  @Parameter(description = """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful
          <p>
          Seems to be present with a value of "v5" in all requests.
          """)
  private String version;
}
