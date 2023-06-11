package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggHotnessV4QueryParams {

  @NotNull
  @Pattern(regexp = "^(boardgame|rpg|videogame)$")
  private String geeksite;

  @NotNull
  @Pattern(regexp = "^(company|event|family|media|person|property|thing)$")
  private String objecttype;

  @Min(1)
  @Max(50)
  private Integer showcount;

}
