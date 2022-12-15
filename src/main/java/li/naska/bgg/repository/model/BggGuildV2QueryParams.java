package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggGuildV2QueryParams {

  /**
   * id=NNN
   * <p>
   * ID of the guild you want to view.
   */
  @NotNull
  @Min(1)
  private Integer id;

  /**
   * members=1
   * <p>
   * Include member roster in the results. Member list is paged and sorted.
   */
  @Min(1)
  @Max(1)
  private Integer members;

  /**
   * sort=SORTTYPE
   * <p>
   * Specifies how to sort the members list; default is username. Valid values are:
   * <ul>
   *   <li>username
   *   <li>date
   * </ul>
   */
  @Pattern(regexp = "^(username|date)$")
  private String sort;

  /**
   * page=NNN
   * <p>
   * The page of the members list to return. Page size is 25.
   */
  @Min(1)
  private Integer page;

}
