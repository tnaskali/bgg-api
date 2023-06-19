package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggHotnessV4QueryParams {

  @NotNull
  @Pattern(regexp = "^(boardgame|rpg|videogame)$")
  @Parameter(
      description = """
          The domain.
          <p>
          <i>Syntax</i> : /hotness?geeksite={domain}
          <p>
          <i>Example</i> : /hotness?geeksite=boardgame
          """,
      schema = @Schema(defaultValue = "boardgame")
  )
  private String geeksite;

  @NotNull
  @Pattern(regexp = "^(company|person|thing)$")
  @Parameter(
      description = """
          The object type.
          <p>
          <i>Syntax</i> : /hotness?geeksite={domain}&objecttype={type}
          <p>
          <i>Example</i> : /hotness?geeksite=boardgame&objecttype=company
          """,
      schema = @Schema(defaultValue = "thing")
  )
  private String objecttype;

  @Min(1)
  @Max(50)
  @Parameter(
      description = """
          The number of results to retrieve.
          <p>
          <i>Syntax</i> : /hotness?geeksite={domain}&objecttype={type}&showcount={count}
          <p>
          <i>Example</i> : /hotness?geeksite=boardgame&objecttype=company&showcount=10
          """,
      schema = @Schema(defaultValue = "50")
  )
  private Integer showcount;

}
