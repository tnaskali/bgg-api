package li.naska.bgg.repository.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggHotV2QueryParams {

  /**
   * type=TYPE
   * <p>
   * There are a number of different hot lists available on the site. Valid types include:
   * <ul>
   *   <li>boardgame
   *   <li>rpg
   *   <li>videogame
   *   <li>boardgameperson
   *   <li>rpgperson
   *   <li>boardgamecompany
   *   <li>rpgcompany
   *   <li>videogamecompany
   * </ul>
   */
  @NotNull
  @Pattern(regexp = "^(boardgame|boardgamecompany|boardgameperson|rpg|rpgcompany|rpgperson|videogame|videogamecompany)$")
  private String type;

}
