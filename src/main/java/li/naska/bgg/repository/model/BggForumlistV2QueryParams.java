package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggForumlistV2QueryParams {

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
