package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggCollectionV2QueryParams {

  @NotNull
  @Parameter(
      example = "eekspider",
      description =
          """
          Name of the user to request the collection for.
          <p>
          <i>Syntax</i> : /collection?username={username}
          <p>
          <i>Example</i> : /collection?username=eekspider
          """)
  private String username;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Returns version info for each item in your collection.
          <p>
          <i>Syntax</i> : /collection?username={username}&version=1
          <p>
          <i>Example</i> : /collection?username=eekspider&version=1
          """)
  private Integer version;

  @Pattern(
      regexp =
          "^(boardgame|boardgameaccessory|boardgameexpansion|boardgameissue|rpgissue|rpgitem|videogame|videogamecompilation|videogameexpansion|videogamehardware)$")
  @Parameter(
      example = "boardgame",
      description =
          """
          Specifies which collection you want to retrieve.
          <p>
          Type may be any thing subtype :
          <li/>boardgame
          <li/>boardgameaccessory
          <li/>boardgameexpansion
          <li/>boardgameissue
          <li/>rpgissue
          <li/>rpgitem
          <li/>videogame
          <li/>videogamecompilation
          <li/>videogameexpansion
          <li/>videogamehardware
          <p>
          The default is "boardgame".
          <p>
          <i>Syntax</i> : /collection?username={username}&subtype={subtype}
          <p>
          <i>Example</i> : /collection?username=eekspider&subtype=boardgame
          """,
      schema = @Schema(defaultValue = "boardgame"))
  private String subtype;

  @Pattern(
      regexp =
          "^(boardgame|boardgameaccessory|boardgameexpansion|boardgameissue|rpgissue|rpgitem|videogame|videogamecompilation|videogameexpansion|videogamehardware)$")
  @Parameter(
      example = "boardgameexpansion",
      description =
          """
          Specifies which subtype you want to exclude from the results.
          <p>
          Subtype may be :
          <li/>boardgame
          <li/>boardgameaccessory
          <li/>boardgameexpansion
          <li/>boardgameissue
          <li/>rpgissue
          <li/>rpgitem
          <li/>videogame
          <li/>videogamecompilation
          <li/>videogameexpansion
          <li/>videogamehardware
          <p>
          <i>Syntax</i> : /collection?username={username}&excludesubtype={subtype}
          <p>
          <i>Example</i> : /collection?username=eekspider&excludesubtype=boardgameexpansion
          """)
  private String excludesubtype;

  @Pattern(regexp = "^[1-9][0-9]*(?:,[1-9][0-9]*)*+$")
  @Parameter(
      example = "16499",
      description =
          """
          Filter collection to specifically listed item(s). Value may be a comma-delimited list of item ids.
          <p>
          <i>Syntax</i> : /collection?username={username}&id={ids}
          <p>
          <i>Example</i> : /collection?username=eekspider&id=935,16499
          """)
  private String id;

  @Min(1)
  @Max(1)
  @Parameter(
      description =
          """
          Returns more abbreviated results.
          <p>
          <i>Syntax</i> : /collection?username={username}&brief=1
          <p>
          <i>Example</i> : /collection?username=eekspider&brief=1
          """)
  private Integer brief;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Returns expanded rating/ranking info for the collection.
          <p>
          <i>Syntax</i> : /collection?username={username}&stats=1
          <p>
          <i>Example</i> : /collection?username=eekspider&stats=1
          """)
  private Integer stats;

  @Min(0)
  @Max(1)
  @Parameter(
      description =
          """
          Filter for owned games. Set to 0 to exclude these items so marked. Set to 1 for returning owned games and 0 for non-owned games.
          <p>
          <i>Syntax</i> : /collection?username={username}&own=1
          <p>
          <i>Example</i> : /collection?username=eekspider&own=1
          """)
  private Integer own;

  @Min(0)
  @Max(1)
  @Parameter(
      description =
          """
          Filter for whether an item has been rated. Set to 0 to exclude these items so marked. Set to 1 to include only these items so marked.
          <p>
          <i>Syntax</i> : /collection?username={username}&rated=1
          <p>
          <i>Example</i> : /collection?username=eekspider&rated=1
          """)
  private Integer rated;

  @Min(0)
  @Max(1)
  @Parameter(
      description =
          """
          Filter for whether an item has been played. Set to 0 to exclude these items so marked. Set to 1 to include only these items so marked.
          <p>
          <i>Syntax</i> : /collection?username={username}&played=1
          <p>
          <i>Example</i> : /collection?username=eekspider&played=1
          """)
  private Integer played;

  @Min(0)
  @Max(1)
  @Parameter(
      description =
          """
          Filter for items that have been commented. Set to 0 to exclude these items so marked. Set to 1 to include only these items so marked.
          <p>
          <i>Syntax</i> : /collection?username={username}&comment=1
          <p>
          <i>Example</i> : /collection?username=eekspider&comment=1
          """)
  private Integer comment;

  @Min(0)
  @Max(1)
  @Parameter(
      description =
          """
          Filter for items marked for trade. Set to 0 to exclude these items so marked. Set to 1 to include only these items so marked.
          <p>
          <i>Syntax</i> : /collection?username={username}&trade=1
          <p>
          <i>Example</i> : /collection?username=eekspider&trade=1
          """)
  private Integer trade;

  @Min(0)
  @Max(1)
  @Parameter(
      description =
          """
          Filter for items wanted in trade. Set to 0 to exclude these items so marked. Set to 1 to include only these items so marked.
          <p>
          <i>Syntax</i> : /collection?username={username}&want=1
          <p>
          <i>Example</i> : /collection?username=eekspider&want=1
          """)
  private Integer want;

  @Min(0)
  @Max(1)
  @Parameter(
      description =
          """
          Filter for items on the wishlist. Set to 0 to exclude these items so marked. Set to 1 to include only these items so marked.
          <p>
          <i>Syntax</i> : /collection?username={username}&wishlist=1
          <p>
          <i>Example</i> : /collection?username=eekspider&wishlist=1
          """)
  private Integer wishlist;

  @Min(1)
  @Max(5)
  @Parameter(
      description =
          """
          Filter for wishlist priority. Returns only items of the specified priority.
          <p>
          <i>Syntax</i> : /collection?username={username}&wishlistpriority={priority}
          <p>
          <i>Example</i> : /collection?username=eekspider&wishlistpriority=5
          """)
  private Integer wishlistpriority;

  @Min(0)
  @Max(1)
  @Parameter(
      description =
          """
          Filter for pre-ordered games Returns only items of the specified priority. Set to 0 to exclude these items so marked. Set to 1 to include only these items so marked.
          <p>
          <i>Syntax</i> : /collection?username={username}&preordered=1
          <p>
          <i>Example</i> : /collection?username=eekspider&preordered=1
          """)
  private Integer preordered;

  @Min(0)
  @Max(1)
  @Parameter(
      description =
          """
          Filter for items marked as wanting to play. Set to 0 to exclude these items so marked. Set to 1 to include only these items so marked.
          <p>
          <i>Syntax</i> : /collection?username={username}&wanttoplay=1
          <p>
          <i>Example</i> : /collection?username=eekspider&wanttoplay=1
          """)
  private Integer wanttoplay;

  @Min(0)
  @Max(1)
  @Parameter(
      description =
          """
          Filter for ownership flag. Set to 0 to exclude these items so marked. Set to 1 to include only these items so marked.
          <p>
          <i>Syntax</i> : /collection?username={username}&wanttobuy=1
          <p>
          <i>Example</i> : /collection?username=eekspider&wanttobuy=1
          """)
  private Integer wanttobuy;

  @Min(0)
  @Max(1)
  @Parameter(
      description =
          """
          Filter for games marked previously owned. Set to 0 to exclude these items so marked. Set to 1 to include only these items so marked.
          <p>
          <i>Syntax</i> : /collection?username={username}&prevowned=1
          <p>
          <i>Example</i> : /collection?username=eekspider&prevowned=1
          """)
  private Integer prevowned;

  @Min(0)
  @Max(1)
  @Parameter(
      description =
          """
          Filter on whether there is a comment in the Has Parts field of the item. Set to 0 to exclude these items so marked. Set to 1 to include only these items so marked.
          <p>
          <i>Syntax</i> : /collection?username={username}&hasparts=1
          <p>
          <i>Example</i> : /collection?username=eekspider&hasparts=1
          """)
  private Integer hasparts;

  @Min(0)
  @Max(1)
  @Parameter(
      description =
          """
          Filter on whether there is a comment in the Wants Parts field of the item. Set to 0 to exclude these items so marked. Set to 1 to include only these items so marked.
          <p>
          <i>Syntax</i> : /collection?username={username}&wantparts=1
          <p>
          <i>Example</i> : /collection?username=eekspider&wantparts=1
          """)
  private String wantparts;

  @Min(-1)
  @Max(10)
  @Parameter(
      description =
          """
          Filter on minimum personal rating assigned for that item in the collection.
          <p>
          <i>Syntax</i> : /collection?username={username}&minrating={rating}
          <p>
          <i>Example</i> : /collection?username=eekspider&minrating=5
          """)
  private Integer minrating;

  @Min(1)
  @Max(10)
  @Parameter(
      description =
          """
          Filter on maximum personal rating assigned for that item in the collection.
          <p>
          <i>Note</i> : Although you'd expect it to be maxrating, it's rating.
          <p>
          <i>Syntax</i> : /collection?username={username}&rating={rating}
          <p>
          <i>Example</i> : /collection?username=eekspider&rating=5
          """)
  private Integer rating;

  @Min(-1)
  @Max(10)
  @Parameter(
      example = "-1",
      description =
          """
          Filter on minimum BGG rating for that item in the collection.
          <p>
          <i>Note</i> : 0 is ignored... you can use -1 though, for example min -1 and max 1 to get items w/no bgg rating.
          <p>
          <i>Syntax</i> : /collection?username={username}&minbggrating={rating}
          <p>
          <i>Example</i> : /collection?username=eekspider&minbggrating=5
          <p>
          <i>Example</i> : /collection?username=eekspider&minbggrating=-1&bggrating=1
          """)
  private Integer minbggrating;

  @Min(1)
  @Max(10)
  @Parameter(
      example = "7",
      description =
          """
          Filter on maximum BGG rating for that item in the collection.
          <p>
          <i>Note</i> : Although you'd expect it to be maxbggrating, it's bggrating.
          <p>
          <i>Syntax</i> : /collection?username={username}&bggrating={rating}
          <p>
          <i>Example</i> : /collection?username=eekspider&bggrating=5
          <i>Example</i> : /collection?username=eekspider&minbggrating=-1&bggrating=1
          """)
  private Integer bggrating;

  @Min(1)
  @Parameter(
      description =
          """
          Filter by minimum number of recorded plays.
          <p>
          <i>Syntax</i> : /collection?username={username}&minplays={plays}
          <p>
          <i>Example</i> : /collection?username=eekspider&minplays=10
          """)
  private Integer minplays;

  @Min(1)
  @Parameter(
      description =
          """
          Filter by maximum number of recorded plays.
          <p>
          <i>Note</i> : Although the two maxima parameters above lack the max part, this one really is maxplays.
          <p>
          <i>Syntax</i> : /collection?username={username}&maxplays={plays}
          <p>
          <i>Example</i> : /collection?username=eekspider&maxplays=99
          """)
  private Integer maxplays;

  @Min(1)
  @Max(1)
  @Parameter(
      example = "1",
      description =
          """
          Filter to show private collection info. Only works when viewing your own collection and you are logged in.
          <p>
          <i>Syntax</i> : /collection?username={username}&showprivate=1
          <p>
          <i>Example</i> : /collection?username=eekspider&showprivate=1
          """)
  private Integer showprivate;

  @Min(1)
  @Parameter(
      example = "7044466",
      description =
          """
          Restrict the collection results to the single specified collection id. Collid is returned in the results of normal queries as well.
          <p>
          <i>Syntax</i> : /collection?username={username}&collid={collid}
          <p>
          <i>Example</i> : /collection?username=eekspider&collid=7044466
          """)
  private Integer collid;

  @Pattern(
      regexp =
          "^([1-2][0-9])?[0-9][0-9]-[0-1][0-9]-[0-3][0-9]( [0-9][0-9]:[0-9][0-9]:[0-9][0-9])?$")
  @Parameter(
      example = "2008-08-09 12:00:00",
      description =
          """
          Restricts the collection results to only those whose status (own, want, fortrade, etc.) has changed or been
          added since the date specified (does not return results for deletions).
          <p>
          Supported formats:
          <li/>YY-MM-DD
          <li/>YYYY-MM-DD
          <li/>YY-MM-DD%20HH:MM:SS
          <li/>YYYY-MM-DD%20HH:MM:SS
          <p>
          <i>Syntax</i> : /collection?username={username}&modifiedsince={date}
          <p>
          <i>Example</i> : /collection?username=eekspider&modifiedsince=2008-08-09%2012:00:00
          """)
  private String modifiedsince;
}
