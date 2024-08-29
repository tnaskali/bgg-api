package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggFamilyV2QueryParams {

  @NotNull
  @Pattern(regexp = "^[1-9][0-9]*(,[1-9][0-9]*)*$")
  @Parameter(
      example = "8590,62408",
      description =
          """
          Specifies the id of the family to retrieve. To request multiple families with a single query, one can specify a comma-delimited list of ids.
          <p>
          <i>Syntax</i> : /family?id={ids}
          <p>
          <i>Example</i> : /family?id=8590,62408
          """)
  private String id;

  @Pattern(
      regexp =
          "^(award|awardcategory|awardset|bgaccessoryfamily|boardgamefamily|boardgamehonor|boardgamepodcast|boardgamesubdomain|rpg|rpgfamily|rpggenre|rpghonor|rpgperiodical|rpgpodcast|rpgseries|rpgsetting|rpgsystem|videogamecharacter|videogamefranchise|videogamehonor|videogameplatform|videogameseries)(,(award|awardcategory|awardset|bgaccessoryfamily|boardgamefamily|boardgamehonor|boardgamepodcast|boardgamesubdomain|rpg|rpgfamily|rpggenre|rpghonor|rpgperiodical|rpgpodcast|rpgseries|rpgsetting|rpgsystem|videogamecharacter|videogamefranchise|videogamehonor|videogameplatform|videogameseries))*$")
  @Parameter(
      example = "award,boardgamefamily",
      description =
          """
          Specifies that, regardless of the type of family asked for by id, the results are filtered by the family type(s) specified. Multiple family types can be specified in a comma-delimited list.
          <p>
          <i>Syntax</i> : /family?id={ids}&type={types}
          <p>
          <i>Example</i> : /family?id=8590,62408&type=award,boardgamefamily
          """)
  private String type;
}
