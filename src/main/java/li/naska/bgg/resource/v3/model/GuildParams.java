package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.SortType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GuildParams {

  @NotNull
  private Integer id;

  private Boolean members;

  private SortType sort;

  private Integer page;

}
