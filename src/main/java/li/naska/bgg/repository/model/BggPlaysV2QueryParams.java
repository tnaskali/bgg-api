package li.naska.bgg.repository.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggPlaysV2QueryParams {

  /**
   * username=NAME
   * <p>
   * Name of the player you want to request play information for. Data is returned in backwards-chronological form. You
   * must include either a username or an id and type to get results.
   */
  private String username;

  /**
   * id=NNN
   * <p>
   * Id number of the item you want to request play information for. Data is returned in backwards-chronological form.
   */
  @Min(1)
  private Integer id;

  /**
   * type=TYPE
   * <p>
   * Type of the item you want to request play information for. Valid types include:
   * <ul>
   *   <li>thing
   *   <li>family
   * </ul>
   */
  @Pattern(regexp = "^(thing|family)$")
  private String type;

  /**
   * mindate=YYYY-MM-DD
   * <p>
   * Returns only plays of the specified date or later.
   */
  @Pattern(regexp = "^[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]$")
  private String mindate;

  /**
   * maxdate=YYYY-MM-DD
   * <p>
   * Returns only plays of the specified date or earlier.
   */
  @Pattern(regexp = "^[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]$")
  private String maxdate;

  /**
   * subtype=TYPE
   * <p>
   * Limits play results to the specified TYPE; boardgame is the default. Valid types include:
   * <ul>
   *   <li>boardgame
   *   <li>boardgameexpansion
   *   <li>boardgameaccessory
   *   <li>boardgameintegration
   *   <li>boardgamecompilation
   *   <li>boardgameimplementation
   *   <li>rpg
   *   <li>rpgitemrpgitem
   *   <li>videogame
   * </ul>
   */
  @Pattern(regexp = "^(boardgame|boardgameexpansion|boardgameaccessory|boardgameintegration|boardgamecompilation|boardgameimplementation|rpg|rpgitemrpgitem|videogame)$")
  private String subtype;

  /**
   * page=NNN
   * <p>
   * The page of information to request. Page size is 100 records.
   */
  @Min(1)
  private Integer page;

  @AssertTrue(message = "You must include either a username or an id and type")
  private boolean isMandatoryFieldsCheck() {
    return username != null || (id != null && type != null);
  }

}
