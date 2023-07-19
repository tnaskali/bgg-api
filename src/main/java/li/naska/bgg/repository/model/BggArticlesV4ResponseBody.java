package li.naska.bgg.repository.model;

import lombok.Data;

import java.util.List;

@Data
public class BggArticlesV4ResponseBody {

  private List<BggArticleV4ResponseBody> articles;

  private Integer pageid;

  private Integer perPage;

  private Integer total;

}
