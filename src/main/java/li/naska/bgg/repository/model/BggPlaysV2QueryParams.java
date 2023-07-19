package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggPlaysV2QueryParams {

  @Parameter(
      example = "eekspider",
      description = """
          Name of the player you want to request play information for.
          <p>
          <i>Syntax</i> : /plays?username={username}
          <p>
          <i>Example</i> : /plays?username=eekspider
          """
  )
  private String username;

  @Min(1)
  @Parameter(
      example = "3085",
      description = """
          Id number of the item you want to request play information for.
          <p>
          <i>Note</i> : Must be used together with "type" unless "username" is also set.
          <p>
          <i>Syntax</i> : /plays?id={itemId}&type={itemType}
          <p>
          <i>Example</i> : /plays?id=3085&type=thing
          """
  )
  private Integer id;

  @Parameter(
      example = "thing",
      description = """
          Type of the item you want to request play information for.
          <p>
          Valid types include:
          <li/>thing
          <li/>family
          <p>
          <i>Note</i> : Must be used together with "id" unless "username" is also set.
          <p>
          <i>Syntax</i> : /plays?id={itemId}&type={itemType}
          <p>
          <i>Example</i> : /plays?id=3085&type=thing
          """
  )
  @Pattern(regexp = "^(thing|family)$")
  private String type;

  @Pattern(regexp = "^[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]$")
  @Parameter(
      example = "2009-01-01",
      description = """
          Returns only plays of the specified date or later.
          <p>
          <i>Syntax</i> : /plays?mindate={date}
          <p>
          <i>Example</i> : /plays?mindate=2009-01-01
          """
  )
  private String mindate;

  @Pattern(regexp = "^[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]$")
  @Parameter(
      example = "2009-12-31",
      description = """
          Returns only plays of the specified date or earlier.
          <p>
          <i>Syntax</i> : /plays?maxdate={date}
          <p>
          <i>Example</i> : /plays?maxdate=2009-12-31
          """
  )
  private String maxdate;

  @Pattern(regexp = "^(boardgame|boardgameexpansion|boardgameaccessory|boardgameintegration|boardgamecompilation|boardgameimplementation|rpg|rpgitem|videogame)$")
  @Parameter(
      example = "boardgame",
      description = """
          Limits play results to the specified subtype; boardgame is the default.
          <p>
          Valid subtypes include:
          <li/>boardgame
          <li/>boardgameexpansion
          <li/>boardgameaccessory
          <li/>boardgameintegration
          <li/>boardgamecompilation
          <li/>boardgameimplementation
          <li/>rpg
          <li/>rpgitem
          <li/>videogame
          <p>
          <i>Note</i> : Must be used together with "id" unless "username" is also set.
          <p>
          <i>Syntax</i> : /plays?subtype={subtype}
          <p>
          <i>Example</i> : /plays?subtype=boardgame
          """,
      schema = @Schema(defaultValue = "boardgame")
  )
  private String subtype;

  @Min(1)
  @Parameter(
      example = "1",
      description = """
          The page of information to request. Page size is 100 records.
          <p>
          <i>Syntax</i> : /plays?page={page}
          <p>
          <i>Example</i> : /plays?page=1
          """
  )
  private Integer page;

  @AssertTrue(message = "You must include either a username or an id and type")
  private boolean isMandatoryFieldsCheck() {
    return username != null || (id != null && type != null);
  }

}
