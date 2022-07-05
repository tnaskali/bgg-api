package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class BggHotnessV4QueryParams {

  @NotNull
  @Pattern(regexp = "^(boardgame|rpg|videogame)$")
  private String geeksite;

  @NotNull
  @Pattern(regexp = "^(company|event|family|media|person|property|thing)")
  private String objecttype;

  @Min(1)
  @Max(50)
  private Integer showcount;

}
