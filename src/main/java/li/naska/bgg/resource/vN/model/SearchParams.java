package li.naska.bgg.resource.vN.model;

import com.boardgamegeek.enums.SearchType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SearchParams {

  @NotNull
  private String query;

  private List<SearchType> type;

  private Boolean exact;

}
