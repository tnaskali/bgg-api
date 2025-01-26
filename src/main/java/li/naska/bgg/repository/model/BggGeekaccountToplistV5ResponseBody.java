package li.naska.bgg.repository.model;

import java.util.List;
import lombok.Data;

@Data
public class BggGeekaccountToplistV5ResponseBody {
  private List<TopListItem> toplistitems;

  @Data
  public static class TopListItem {
    private Integer rank;
    private Integer toplistitemid;
    private String type;
    private Integer id;
    private String name;
    private String href;
  }
}
