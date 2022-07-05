package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class BggSearchV1QueryParams {

  /**
   * search=SEARCH_QUERY
   * <p>
   * String to search for (required)
   */
  @NotNull
  private String search;

  /**
   * exact=1
   * <p>
   * Exact name/aka search only (set it to 1, absent by default) [exact doesn't seem to work yet in BGG 2.0]
   */
  @Min(1)
  @Max(1)
  private String exact;

}
