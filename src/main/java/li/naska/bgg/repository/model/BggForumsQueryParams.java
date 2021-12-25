package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class BggForumsQueryParams {

  /**
   * id=NNN
   * <p>
   * Specifies the id of the type of database entry you want the forum list for. This is the id that appears in the
   * address of the page when visiting a particular game in the database.
   */
  @NotNull
  @Min(1)
  private Integer id;

  /**
   * type=[thing,family]
   * <p>
   * The type of entry in the database.
   */
  @NotNull
  @Pattern(regexp = "^(thing|family)$")
  private String type;

}
