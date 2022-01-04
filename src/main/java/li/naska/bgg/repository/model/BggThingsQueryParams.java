package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class BggThingsQueryParams {

  /**
   * id=NNN
   * <p>
   * Specifies the id of the thing(s) to retrieve. To request multiple things with a single query, NNN can specify a
   * comma-delimited list of ids.
   */
  @NotNull
  @Pattern(regexp = "^[1-9][0-9]*(,[1-9][0-9]*)*$")
  private String id;

  /**
   * type=THINGTYPE
   * <p>
   * Specifies that, regardless of the type of thing asked for by id, the results are filtered by the THINGTYPE(s)
   * specified. Multiple THINGTYPEs can be specified in a comma-delimited list.
   */
  @Pattern(regexp = "^(boardgame|boardgameaccessory|boardgameexpansion|rpgissue|rpgitem|videogame)(,(boardgame|boardgameaccessory|boardgameexpansion|rpgissue|rpgitem|videogame))*$")
  private String type;

  /**
   * versions=1
   * <p>
   * Returns version info for the item.
   */
  @Min(1)
  @Max(1)
  private Integer versions;

  /**
   * videos=1
   * <p>
   * Returns videos for the item.
   */
  @Min(1)
  @Max(1)
  private Integer videos;

  /**
   * stats=1
   * <p>
   * Returns ranking and rating stats for the item.
   */
  @Min(1)
  @Max(1)
  private Integer stats;

  /**
   * historical=1
   * <p>
   * <i>Not currently supported.</i> Returns historical data over time. See page parameter.
   */
  @Min(1)
  @Max(1)
  private Integer historical;

  /**
   * marketplace=1
   * <p>
   * Returns marketplace data.
   */
  @Min(1)
  @Max(1)
  private Integer marketplace;

  /**
   * comments=1
   * <p>
   * Returns all comments about the item. Also includes ratings when commented. See page parameter.
   */
  @Min(1)
  @Max(1)
  private Integer comments;

  /**
   * ratingcomments=1
   * <p>
   * Returns all ratings for the item. Also includes comments when rated. See page parameter. The ratingcomments and
   * comments parameters cannot be used together, as the output always appears in the <comments> node of the XML;
   * comments parameter takes precedence if both are specified. Ratings are sorted in descending rating value, based on
   * the highest rating they have assigned to that item (each item in the collection can have a different rating).
   */
  @Min(1)
  @Max(1)
  private Integer ratingcomments;

  /**
   * page=NNN
   * <p>
   * Defaults to 1, controls the page of data to see for historical info, comments, and ratings data.
   */
  @Min(1)
  private Integer page;

  /**
   * pagesize=NNN
   * <p>
   * Set the number of records to return in paging. Minimum is 10, maximum is 100.
   */
  @Min(10)
  @Max(100)
  private Integer pagesize;

  /**
   * from=YYYY-MM-DD
   * <p>
   * <i>Not currently supported.</i>
   */
  @Deprecated
  @Pattern(regexp = "^[0-3][0-9]-[0-1][0-9]-[1-2][0-9][0-9][0-9]$")
  private String from;

  /**
   * to=YYYY-MM-DD
   * <p>
   * <i>Not currently supported.</i>
   */
  @Deprecated
  @Pattern(regexp = "^[0-3][0-9]-[0-1][0-9]-[1-2][0-9][0-9][0-9]$")
  private String to;

  @AssertTrue(message = "only one of comments and ratingcomments is allowed")
  private boolean isCommentsValid() {
    return !(Integer.valueOf(1).equals(comments) && Integer.valueOf(1).equals(ratingcomments));
  }

}
