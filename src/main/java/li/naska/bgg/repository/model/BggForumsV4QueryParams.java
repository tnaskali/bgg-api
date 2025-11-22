package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggForumsV4QueryParams {

  @NotNull
  @Min(1)
  @Parameter(example = "205637", description = """
          The object id.
          <p>
          <i>Syntax</i> : /forums?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /forums?objectid=205637&objecttype=thing
          """)
  private Integer objectid;

  @NotNull
  @Pattern(
      regexp = "^(?:company|component|event|family|media|person|property|thing|version|weblink)$")
  @Parameter(example = "thing", description = """
          The object type.
          <p>
          <i>Syntax</i> : /forums?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /forums?objectid=205637&objecttype=thing
          """)
  private String objecttype;
}
