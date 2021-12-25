package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.SearchType;
import com.boardgamegeek.search.Name;
import lombok.Data;

import java.util.List;

@Data
public class Results {
  private Integer total;
  private List<Result> item;

  @Data
  public static class Result {
    private Name name;
    private Integer yearpublished;
    private SearchType type;
    private Integer id;
  }
}
