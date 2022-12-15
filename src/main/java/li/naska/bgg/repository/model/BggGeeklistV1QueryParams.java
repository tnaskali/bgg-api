package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * Note: the old "start" and "count" parameters are obsolete and no longer supported. They were required in the original
 * api if you wanted to retreive all items on a geeklist longer than 150 items as that's the most that could be returned
 * on a single api call. Currently however the geeklist xml api returns the entire geeklist (all items) in a single
 * call.
 */
@Data
public class BggGeeklistV1QueryParams {

  /**
   * comments=1
   * <p>
   * Retrieve comments? (default absent, set to 1 if you want comments)
   */
  @Min(1)
  @Max(1)
  private Integer comments;

  @Deprecated
  private Integer start;

  @Deprecated
  private Integer count;

}
