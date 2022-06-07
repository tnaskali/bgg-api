package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
public class BggBoardgameV1QueryParams {

  /**
   * comments=1
   * <p>
   * Show users' comments on games (set it to 1, absent by default)
   */
  @Min(1)
  @Max(1)
  private Integer comments;

  /**
   * stats=1
   * <p>
   * Include game statistics (set it to 1, absent by default)
   */
  @Min(1)
  @Max(1)
  private Integer stats;

  /**
   * historical=1
   * <p>
   * Include historical game statistics (set it to 1, absent by default) - Use from/end parameters to set starting and
   * ending dates. Returns all data starting from 2006-03-18.
   */
  @Min(1)
  @Max(1)
  private Integer historical;

  /**
   * from=YYYY-MM-DD
   * <p>
   * Set the start date to include historical data (format: YYYY-MM-DD, absent by default)
   */
  @Pattern(regexp = "^[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]$")
  private String from;

  /**
   * to=YYYY-MM-DD
   * <p>
   * Set the end date to include historical data (format: YYYY-MM-DD, absent by default)
   */
  @Pattern(regexp = "^[1-2][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]$")
  private String to;

}
