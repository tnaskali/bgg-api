package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggGeekcollectionV3QueryParams {

  // technical properties

  @NotNull
  @Pattern(regexp = "^(exportcsv)$")
  private String action;
  @Pattern(regexp = "^(csv)$")
  private String exporttype;

  // business properties

  @NotNull
  @Pattern(regexp = "^(boardgame|rpg|videogame)$")
  private String subtype;
  @NotNull
  private String username;
  @Min(1)
  @Max(1)
  private Integer all;

}
