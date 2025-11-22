package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggHotV2QueryParams {

  @NotNull
  @Pattern(
      regexp =
          "^(?:boardgame|boardgamecompany|boardgameperson|rpg|rpgcompany|rpgperson|videogame|videogamecompany)$")
  @Parameter(example = "boardgame", description = """
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
          <i>Syntax</i> : /hot?type={type}
          <p>
          <i>Example</i> : /hot?type=boardgame
          """, schema = @Schema(defaultValue = "boardgame"))
  private String type;
}
