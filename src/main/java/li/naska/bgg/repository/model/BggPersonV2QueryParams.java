package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggPersonV2QueryParams {

  @NotNull
  @Pattern(regexp = "^[1-9][0-9]*(?:,[1-9][0-9]*)*+$")
  @Parameter(
      example = "153580,150831",
      description =
          """
          Specifies the id of the person(s) to retrieve. To request multiple persons with a single query, one can specify a
          comma-delimited list of ids.
          <p>
          <i>Syntax</i> : /person?id={ids}
          <p>
          <i>Example</i> : /person?id=153580,150831
          """)
  private String id;

  @Pattern(
      regexp =
          "^(boardgameartist|boardgameauthor|boardgamedesigner|boardgamedeveloper|boardgameeditor|boardgamegraphicdesigner|boardgameinsertdesigner|boardgamesculptor|boardgamesolodesigner|boardgamewriter|puzzleartist|puzzledesigner|rpgartist|rpgdesigner|rpgproducer)(,(boardgameartist|boardgameauthor|boardgamedesigner|boardgamedeveloper|boardgameeditor|boardgamegraphicdesigner|boardgameinsertdesigner|boardgamesculptor|boardgamesolodesigner|boardgamewriter|puzzleartist|puzzledesigner|rpgartist|rpgdesigner|rpgproducer))*$")
  @Parameter(
      example = "boardgamedeveloper,boardgameeditor",
      description =
          """
          Specifies that, regardless of the type of person asked for by id, the results are filtered by the person
          type(s) specified. Multiple person type can be specified in a comma-delimited list.
          <p>
          The XMLAPI2 supports persons of the following person types:
          <li/>boardgameartist
          <li/>boardgameauthor
          <li/>boardgamedesigner
          <li/>boardgamedeveloper
          <li/>boardgameeditor
          <li/>boardgamegraphicdesigner
          <li/>boardgameinsertdesigner
          <li/>boardgamesculptor
          <li/>boardgamesolodesigner
          <li/>boardgamewriter
          <li/>puzzleartist
          <li/>puzzledesigner
          <li/>rpgartist
          <li/>rpgdesigner
          <li/>rpgproducer
          <p>
          <i>Syntax</i> : /person?id={ids}&type={personTypes}
          <p>
          <i>Example</i> : /person?id=153580,150831&type=boardgamedeveloper,boardgameeditor
          """)
  private String type;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Returns videos for the person.
          <p>
          <i>Syntax</i> : /person?id={ids}&videos=1
          <p>
          <i>Example</i> : /person?id=153580&videos=1
          """)
  private Integer videos;
}
