package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggThreadV2QueryParams {

  /**
   * id=NNN
   * <p>
   * Specifies the id of the thread to retrieve.
   */
  @NotNull
  @Min(1)
  private Integer id;

  /**
   * minarticleid=NNN
   * <p>
   * Filters the results so that only articles with an equal or higher id than NNN will be returned.
   */
  @Min(1)
  private Integer minarticleid;

  /**
   * minarticledate=YYYY-MM-DD
   * <br/>
   * minarticledate=YYYY-MM-DD%20HH%3AMM%3ASS
   * <p>
   * Filters the results so that only articles on the specified date or later will be returned.
   */
  @Pattern(regexp = "^([1-2][0-9])?[0-9][0-9]-[0-1][0-9]-[0-3][0-9]( [0-9][0-9]:[0-9][0-9]:[0-9][0-9])?$")
  private String minarticledate;

  /**
   * count=NNN
   * <p>
   * Limits the number of articles returned to no more than NNN.
   */
  @Min(1)
  private Integer count;

  /**
   * username=NAME
   * <p>
   * <i>Not currently supported.</i>
   */
  @Deprecated
  private String username;

}
