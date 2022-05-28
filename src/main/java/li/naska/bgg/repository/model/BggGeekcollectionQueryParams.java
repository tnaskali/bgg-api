package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class BggGeekcollectionQueryParams {

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
