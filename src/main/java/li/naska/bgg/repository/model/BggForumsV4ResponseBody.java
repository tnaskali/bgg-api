package li.naska.bgg.repository.model;

import java.util.List;
import lombok.Data;

@Data
public class BggForumsV4ResponseBody {
  private List<Forum> forums;

  @Data
  public static class Forum {
    private String title;
    private Integer forumid;
    private Integer forumuid;
    private Integer numposts;
    private Integer numthreads;
  }
}
