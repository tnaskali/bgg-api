package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggHotV2QueryParams {

  @NotNull
  @Pattern(regexp = "^(boardgame|boardgamecompany|boardgameperson|rpg|rpgcompany|rpgperson|videogame|videogamecompany)$")
  @Parameter(
      example = "boardgame",
      description = """
          There are a number of different hot lists available on the site.
          <p>
          Valid types include:
          <li/>boardgame
          <li/>boardgamecompany
          <li/>boardgameperson
          <li/>rpg
          <li/>rpgcompany
          <li/>rpgperson
          <li/>videogame
          <li/>videogamecompany
          <p>
          <i>Note</i> : "boardgamecompany", "rpgperson" and "videogamecompany" don't seem to return any data.
          <p>
          <i>Syntax</i> : /hot?type={type}
          <p>
          <i>Example</i> : /hot?type=boardgame
          """
  )
  private String type;

}
