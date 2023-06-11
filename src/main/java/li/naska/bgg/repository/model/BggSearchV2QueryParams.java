package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BggSearchV2QueryParams {

  @NotNull
  @Parameter(
      example = "Crossbows+and+Catapults",
      description = """
          Returns all types of Items that match the query string. Spaces in the query string are replaced by a +.
          <p>
          <i>Syntax</i> : /search?query={queryString}
          <p>
          <i>Example</i> : /search?query=Crossbows+and+Catapults
          """
  )
  private String query;

  @Parameter(
      example = "boardgame",
      description = """
          Return all items that match the query string of a specific type. You can return multiple types by listing them
          separated by commas, e.g. type=TYPE1,TYPE2,TYPE3.
          <p>
          Type might be one of these object types (all but "component", "media" and "version") :
          <li/>company
          <li/>event
          <li/>family
          <li/>person
          <li/>property
          <li/>thing
          <li/>weblink
          <p>
          Or any of their subtypes:
          <li/>boardgame
          <li/>boardgameaccessory
          <li/>boardgameartist
          <li/>rpg
          <li/>rpgissue
          <li/>rpgitem
          <li/>videogame
          <li/>...
          <p>
          <i>Syntax</i> : /search?query={queryString}&type={type}
          <p>
          <i>Example</i> : /search?query=Crossbows+and+Catapults&type=boardgame
          """,
      schema = @Schema(defaultValue = "thing")
  )
  private String type;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description = """
          Limit results to items that match the query exactly.
          <p>
          <i>Syntax</i> : /search?query={queryString}&exact=1
          <p>
          <i>Example</i> : /search?query=Crossbows+and+Catapults&exact=1
          """
  )
  private String exact;

}
