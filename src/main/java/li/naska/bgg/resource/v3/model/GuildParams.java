package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.SortType;
import lombok.Data;

@Data
public class GuildParams {

  private Boolean members;

  private SortType sort;

  private Integer page;

}
