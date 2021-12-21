package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class BggCollectionQueryParams {

  /**
   * username=NAME
   * <p>
   * Name of the user to request the collection for.
   */
  @NotNull
  private String username;

  /**
   * version=1
   * <p>
   * Returns version info for each item in your collection.
   */
  @Min(1)
  @Max(1)
  private Integer version;

  /**
   * subtype=TYPE
   * <p>
   * Specifies which collection you want to retrieve. TYPE may be boardgame, boardgameexpansion, boardgameaccessory,
   * rpgitem, rpgissue, or videogame; the default is boardgame
   */
  @Pattern(regexp = "^(boardgame|boardgameexpansion|boardgameaccessory|rpgitem|rpgissue|videogame|videogamecompilation|videogameexpansion)$")
  private String subtype;

  /**
   * excludesubtype=TYPE
   * <p>
   * Specifies which subtype you want to exclude from the results.
   */
  @Pattern(regexp = "^(boardgame|boardgameexpansion|boardgameaccessory|rpgitem|rpgissue|videogame|videogamecompilation|videogameexpansion)$")
  private String excludesubtype;

  /**
   * id=NNN
   * <p>
   * Filter collection to specifically listed item(s). NNN may be a comma-delimited list of item ids.
   */
  @Pattern(regexp = "^[1-9][0-9]*(,[1-9][0-9]*)*$")
  private String id;

  /**
   * brief=1
   * <p>
   * Returns more abbreviated results.
   */
  @Min(1)
  @Max(1)
  private Integer brief;

  /**
   * stats=1
   * <p>
   * Returns expanded rating/ranking info for the collection.
   */
  @Min(1)
  @Max(1)
  private Integer stats;

  /**
   * own=[0,1]
   * <p>
   * Filter for owned games. Set to 0 to exclude these items so marked. Set to 1 for returning owned games and 0 for
   * non-owned games.
   */
  @Min(0)
  @Max(1)
  private Integer own;

  /**
   * rated=[0,1]
   * <p>
   * Filter for whether an item has been rated. Set to 0 to exclude these items so marked. Set to 1 to include only
   * these items so marked.
   */
  @Min(0)
  @Max(1)
  private Integer rated;

  /**
   * played=[0,1]
   * <p>
   * Filter for whether an item has been played. Set to 0 to exclude these items so marked. Set to 1 to include only
   * these items so marked.
   */
  @Min(0)
  @Max(1)
  private Integer played;

  /**
   * comment=[0,1]
   * <p>
   * Filter for items that have been commented. Set to 0 to exclude these items so marked. Set to 1 to include only
   * these items so marked.
   */
  @Min(0)
  @Max(1)
  private Integer comment;

  /**
   * trade=[0,1]
   * <p>
   * Filter for items marked for trade. Set to 0 to exclude these items so marked. Set to 1 to include only these items
   * so marked.
   */
  @Min(0)
  @Max(1)
  private Integer trade;

  /**
   * want=[0,1]
   * <p>
   * Filter for items wanted in trade. Set to 0 to exclude these items so marked. Set to 1 to include only these items
   * so marked.
   */
  @Min(0)
  @Max(1)
  private Integer want;

  /**
   * wishlist=[0,1]
   * <p>
   * Filter for items on the wishlist. Set to 0 to exclude these items so marked. Set to 1 to include only these items
   * so marked.
   */
  @Min(0)
  @Max(1)
  private Integer wishlist;

  /**
   * wishlistpriority=[1-5]
   * <p>
   * Filter for wishlist priority. Returns only items of the specified priority.
   */
  @Min(1)
  @Max(5)
  private Integer wishlistpriority;

  /**
   * preordered=[0,1]
   * <p>
   * Filter for pre-ordered games Returns only items of the specified priority. Set to 0 to exclude these items so
   * marked. Set to 1 to include only these items so marked.
   */
  @Min(0)
  @Max(1)
  private Integer preordered;

  /**
   * wanttoplay=[0,1]
   * <p>
   * Filter for items marked as wanting to play. Set to 0 to exclude these items so marked. Set to 1 to include only
   * these items so marked.
   */
  @Min(0)
  @Max(1)
  private Integer wanttoplay;

  /**
   * wanttobuy=[0,1]
   * <p>
   * Filter for ownership flag. Set to 0 to exclude these items so marked. Set to 1 to include only these items so
   * marked.
   */
  @Min(0)
  @Max(1)
  private Integer wanttobuy;

  /**
   * prevowned=[0,1]
   * <p>
   * Filter for games marked previously owned. Set to 0 to exclude these items so marked. Set to 1 to include only these
   * items so marked.
   */
  @Min(0)
  @Max(1)
  private Integer prevowned;

  /**
   * hasparts=[0,1]
   * <p>
   * Filter on whether there is a comment in the Has Parts field of the item. Set to 0 to exclude these items so marked.
   * Set to 1 to include only these items so marked.
   */
  @Min(0)
  @Max(1)
  private Integer hasparts;

  /**
   * wantparts=[0,1]
   * <p>
   * Filter on whether there is a comment in the Wants Parts field of the item. Set to 0 to exclude these items so
   * marked. Set to 1 to include only these items so marked.
   */
  @Min(0)
  @Max(1)
  private String wantparts;

  /**
   * minrating=[1-10]
   * <p>
   * Filter on minimum personal rating assigned for that item in the collection.
   */
  @Min(-1)
  @Max(10)
  private Integer minrating;

  /**
   * rating=[1-10]
   * <p>
   * Filter on maximum personal rating assigned for that item in the collection. [Note: Although you'd expect it to be
   * maxrating, it's rating.]
   */
  @Min(1)
  @Max(10)
  private Integer rating;

  /**
   * minbggrating=[1-10]
   * <p>
   * Filter on minimum BGG rating for that item in the collection. Note: 0 is ignored... you can use -1 though, for
   * example min -1 and max 1 to get items w/no bgg rating.
   */
  @Min(-1)
  @Max(10)
  private Integer minbggrating;

  /**
   * bggrating=[1-10]
   * <p>
   * Filter on maximum BGG rating for that item in the collection. [Note: Although you'd expect it to be maxbggrating,
   * it's bggrating.]
   */
  @Min(1)
  @Max(10)
  private Integer bggrating;

  /**
   * minplays=NNN
   * <p>
   * Filter by minimum number of recorded plays.
   */
  @Min(1)
  private Integer minplays;

  /**
   * maxplays=NNN
   * <p>
   * Filter by maximum number of recorded plays. [Note: Although the two maxima parameters above lack the max part, this
   * one really is maxplays.]
   */
  @Min(1)
  private Integer maxplays;

  /**
   * showprivate=1
   * <p>
   * Filter to show private collection info. Only works when viewing your own collection and you are logged in.
   */
  @Min(1)
  @Max(1)
  private Integer showprivate;

  /**
   * collid=NNN
   * <p>
   * Restrict the collection results to the single specified collection id. Collid is returned in the results of normal
   * queries as well.
   */
  @Min(1)
  private Integer collid;

  /**
   * modifiedsince=YY-MM-DD
   * <br/>
   * modifiedsince=YY-MM-DD%20HH%3AMM%3ASS
   * <p>
   * Restricts the collection results to only those whose status (own, want, fortrade, etc.) has changed or been added
   * since the date specified (does not return results for deletions). Time may be added as well:
   * modifiedsince=YY-MM-DD%20HH:MM:SS
   */
  @Pattern(regexp = "^([1-2][0-9])?[0-9][0-9]-[0-1][0-9]-[0-3][0-9]( [0-9][0-9]:[0-9][0-9]:[0-9][0-9])?$")
  private String modifiedsince;

}
