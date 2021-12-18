package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BggGuildQueryParams {

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
