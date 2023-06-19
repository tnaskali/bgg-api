package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BggSearchV1QueryParams {

  @NotNull
  @Parameter(
      example = "Crossbows and Catapults",
      description = """
          String to search for (required).
          <p>
          <i>Syntax</i> : /search?search={searchString}
          <p>
          <i>Example</i> : /search?search=Crossbows%20and%20Catapults
          """
  )
  private String search;

  @Deprecated
  @Min(1)
  @Max(1)
  @Parameter(
      description = """
          Exact name/aka search only (set it to 1, absent by default).
          <p>
          <i>Note</i> : exact doesn't seem to work yet in BGG 2.0
          <p>
          <i>Syntax</i> : /search?search={searchString}&exact=1
          <p>
          <i>Example</i> : /search?search=Crossbows%20and%20Catapults&exact=1
          """
  )
  private String exact;

}
