package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BggForumV2QueryParams {

  /**
   * id=NNN
   * <p>
   * Specifies the id of the forum. This is the id that appears in the address of the page when visiting a forum in the
   * browser.
   */
  @NotNull
  @Min(1)
  private Integer id;

  /**
   * page=NNN
   * <p>
   * The page of the thread list to return; page size is 50. Threads in the thread list are sorted in order of most
   * recent post.
   */
  @Min(1)
  private Integer page;

}
