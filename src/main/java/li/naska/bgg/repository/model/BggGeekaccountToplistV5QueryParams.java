package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggGeekaccountToplistV5QueryParams {

  @NotNull
  @Pattern(regexp = "^(?:hot|top)$")
  @Parameter(
      example = "top",
      description =
          """
          List type.
          <p>
          Possible values are:
          <li/>hot
          <li/>top
          """)
  private String listtype;

  @NotNull
  @Pattern(regexp = "^(?:boardgame|rpg|videogame)$")
  @Parameter(
      example = "boardgame",
      description =
          """
          Domain.
          <p>
          Possible values are any domain:
          <li/>boardgame
          <li/>rpg
          <li/>videogame
          """)
  private String domain;
}
