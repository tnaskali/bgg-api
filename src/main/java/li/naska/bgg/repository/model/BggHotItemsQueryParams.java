package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BggHotItemsQueryParams {

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
