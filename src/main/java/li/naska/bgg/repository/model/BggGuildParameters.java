package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.SortType;
import lombok.Data;

@Data
public class BggGuildParameters {

  private final Integer id;

  private Boolean members;

  private SortType sort;

  private Integer page;

}
