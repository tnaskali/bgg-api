package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class BggFamilyV2QueryParams {

  /**
   * id=NNN
   * <p>
   * Specifies the id of the family to retrieve. To request multiple families with a single query, NNN can specify a
   * comma-delimited list of ids.
   */
  @NotNull
  @Pattern(regexp = "^[1-9][0-9]*(,[1-9][0-9]*)*$")
  private String id;

  /**
   * type=FAMILYTYPE
   * <p>
   * Specifies that, regardless of the type of family asked for by id, the results are filtered by the FAMILYTPE(s)
   * specified. Multiple FAMILYTYPEs can be specified in a comma-delimited list.
   */
  @Pattern(regexp = "^(award|awardcategory|awardset|bgaccessoryfamily|boardgamefamily|boardgamehonor|boardgamepodcast|boardgamesubdomain|rpg|rpgfamily|rpggenre|rpghonor|rpgperiodical|rpgpodcast|rpgseries|rpgsetting|rpgsystem|videogamecharacter|videogamefranchise|videogamehonor|videogameplatform|videogameseries)(,(award|awardcategory|awardset|bgaccessoryfamily|boardgamefamily|boardgamehonor|boardgamepodcast|boardgamesubdomain|rpg|rpgfamily|rpggenre|rpghonor|rpgperiodical|rpgpodcast|rpgseries|rpgsetting|rpgsystem|videogamecharacter|videogamefranchise|videogamehonor|videogameplatform|videogameseries))*$")
  private String type;

}
