package li.naska.bgg.repository.model;

import java.util.List;
import lombok.Data;

@Data
public class BggArticlesV4ResponseBody {

  private List<BggArticleV4ResponseBody> articles;

  private Integer pageid;

  private Integer perPage;

  private Integer total;

  private Errors errors;

  @Data
  public static class Errors {
    private String error;
  }
}
