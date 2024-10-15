package li.naska.bgg.repository.model;

import java.util.List;
import lombok.Data;

@Data
public class BggCommentReactionsV4ResponseBody {
  private Integer thumbs;
  private List<Link> links;
  private List<Integer> users;

  @Data
  public static class Link {
    private String rel;
    private String uri;
  }
}
