package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggSearchV2QueryParams {

  /**
   * query=SEARCH_QUERY
   * <p>
   * Returns all types of Items that match SEARCH_QUERY. Spaces in the SEARCH_QUERY are replaced by a +
   */
  @NotNull
  private String query;

  /**
   * type=TYPE
   * <p>
   * Return all items that match SEARCH_QUERY of type TYPE. TYPE might be:
   * <ul>
   *   <li>rpgitem
   *   <li>videogame
   *   <li>boardgame
   *   <li>boardgameaccessory
   *   <li>boardgameexpansion
   * </ul>
   * You can return multiple types by listing them separated by commas, e.g type=TYPE1,TYPE2,TYPE3
   */
  @Pattern(regexp = "^(boardgame|boardgameaccessory|boardgameartist|boardgameexpansion|boardgamefamily|boardgamehonor|boardgameperson|boardgamepublisher|family|rpg|rpgissue|rpgitem|rpgperiodical|thing|videogame)(,(boardgame|boardgameaccessory|boardgameartist|boardgameexpansion|boardgamefamily|boardgamehonor|boardgameperson|boardgamepublisher|family|rpg|rpgissue|rpgitem|rpgperiodical|thing|videogame))*$")
  private String type;

  /**
   * exact=1
   * <p>
   * Limit results to items that match the SEARCH_QUERY exactly
   */
  @Min(1)
  @Max(1)
  private String exact;

}
