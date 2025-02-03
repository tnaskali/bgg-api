package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggFansV4QueryParams {

  @NotNull
  @Min(1)
  @Parameter(
      example = "177",
      description =
          """
          The object id.
          <p>
          <i>Syntax</i> : /fans?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /fans?objectid=177&objecttype=thing
          """)
  private Integer objectid;

  @NotNull
  @Pattern(
      regexp = "^(?:company|component|event|family|media|person|property|thing|version|weblink)$")
  @Parameter(
      example = "thing",
      description =
          """
          The object type.
          <p>
          <i>Syntax</i> : /fans?objectid={id}&objecttype={type}
          <p>
          <i>Example</i> : /fans?objectid=177&objecttype=thing
          """)
  private String objecttype;

  @Min(1)
  @Parameter(
      example = "825923",
      description =
          """
          The user id of a fan.
          <p>
          <i>Syntax</i> : /fans?objectid={id}&objecttype={type}&userid={userid}
          <p>
          <i>Example</i> : /fans?objectid=177&objecttype=thing&userid=825923
          """)
  private Integer userid;

  @Deprecated
  @Min(1)
  @Parameter(
      description =
          """
          Mined from bgg website, but marked as deprecated as it isn't required and doesn't seem useful.
          <p>
          Seems to be present with a value of "1" in all requests.
          """)
  private Integer nosession;
}
