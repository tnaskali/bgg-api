package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggThingV2QueryParams {

  @NotNull
  @Pattern(regexp = "^[1-9][0-9]*(?:,[1-9][0-9]*)*+$")
  @Parameter(
      example = "1000,383974",
      description =
          """
          Specifies the id of the thing(s) to retrieve. To request multiple things with a single query, one can specify a comma-delimited list of ids.
          <p>
          <i>Syntax</i> : /thing?id={ids}
          <p>
          <i>Example</i> : /thing?id=1000,383974
          """)
  private String id;

  @Pattern(
      regexp =
          "^(bgsleeve|boardgame|boardgameaccessory|boardgameexpansion|boardgameissue|puzzle|rpgissue|rpgitem|videogame|videogamecompilation|videogameexpansion|videogamehardware)(,(bgsleeve|boardgame|boardgameaccessory|boardgameexpansion|boardgameissue|puzzle|rpgissue|rpgitem|videogame|videogamecompilation|videogameexpansion|videogamehardware))*$")
  @Parameter(
      example = "bgsleeve,boardgame",
      description =
          """
          Specifies that, regardless of the type of thing asked for by id, the results are filtered by the
          thing type(s). Multiple thing types can be specified in a comma-delimited list.
          <p>
          The XMLAPI2 supports things of the following thing types:
          <li/>bgsleeve
          <li/>boardgame
          <li/>boardgameaccessory
          <li/>boardgameexpansion
          <li/>boardgameissue
          <li/>puzzle
          <li/>rpgissue
          <li/>rpgitem
          <li/>videogame
          <li/>videogamecompilation
          <li/>videogameexpansion
          <li/>videogamehardware
          <p>
          <i>Syntax</i> : /thing?id={ids}&type={types}
          <p>
          <i>Example</i> : /thing?id=1000,383974&type=bgsleeve,boardgame
          """)
  private String type;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Returns version info for the item.
          <p>
          <i>Syntax</i> : /thing?id={ids}&versions=1
          <p>
          <i>Example</i> : /thing?id=205637&versions=1
          """)
  private Integer versions;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Returns videos for the item.
          <p>
          <i>Syntax</i> : /thing?id={ids}&videos=1
          <p>
          <i>Example</i> : /thing?id=205637&videos=1
          """)
  private Integer videos;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Returns ranking and rating stats for the item.
          <p>
          <i>Syntax</i> : /thing?id={ids}&stats=1
          <p>
          <i>Example</i> : /thing?id=205637&stats=1
          """)
  private Integer stats;

  @Deprecated
  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          <i>Not currently supported.</i> Returns historical data over time. See page parameter.
          <p>
          <i>Syntax</i> : /thing?id={ids}&historical=1
          <p>
          <i>Example</i> : /thing?id=205637&historical=1
          """)
  private Integer historical;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Returns marketplace data.
          <p>
          <i>Syntax</i> : /thing?id={ids}&marketplace=1
          <p>
          <i>Example</i> : /thing?id=205637&marketplace=1
          """)
  private Integer marketplace;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Returns all comments about the item. Also includes ratings when commented. See page parameter.
          <p>
          <i>Syntax</i> : /thing?id={ids}&comments=1
          <p>
          <i>Example</i> : /thing?id=205637&comments=1
          """)
  private Integer comments;

  @Min(1)
  @Max(1)
  @Parameter(
      description =
          """
          Returns all ratings for the item. Also includes comments when rated. See page parameter. Ratings are sorted in
          descending rating value, based on the highest rating they have assigned to that item (each item in the
          collection can have a different rating).
          <p>
          <i>Note</i> : The ratingcomments and comments parameters cannot be used together, as the output always appears
          in the &lt;comments&gt; node of the XML; comments parameter takes precedence if both are specified.
          <p>
          <i>Syntax</i> : /thing?id={ids}&ratingcomments=1
          <p>
          <i>Example</i> : /thing?id=205637&ratingcomments=1
          """)
  private Integer ratingcomments;

  @Min(1)
  @Parameter(
      description =
          """
          Defaults to 1, controls the page of data to see for historical info, comments, and ratings data.
          <p>
          <i>Syntax</i> : /thing?id={ids}&page=1
          <p>
          <i>Example</i> : /thing?id=205637&page=1
          """,
      schema = @Schema(defaultValue = "1"))
  private Integer page;

  @Min(10)
  @Max(100)
  @Parameter(
      description =
          """
          Set the number of records to return in paging. Minimum is 10, maximum is 100. Default is 100.
          <p>
          <i>Syntax</i> : /thing?id={ids}&pagesize=10
          <p>
          <i>Example</i> : /thing?id=205637&pagesize=10
          """,
      schema = @Schema(defaultValue = "100"))
  private Integer pagesize;

  @Deprecated
  @Pattern(regexp = "^[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]$")
  @Parameter(
      description =
          """
          <i>Not currently supported.</i>
          <p>
          <i>Syntax</i> : /thing?id={ids}&from={date}
          <p>
          <i>Example</i> : /thing?id=205637&from=2006-03-18
          """)
  private String from;

  @Deprecated
  @Pattern(regexp = "^[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]$")
  @Parameter(
      description =
          """
          <i>Not currently supported.</i>
          <p>
          <i>Syntax</i> : /thing?id={ids}&to={date}
          <p>
          <i>Example</i> : /thing?id=205637&to=2999-12-31
          """)
  private String to;
}
