package li.naska.bgg.resource.vN.model;

import com.boardgamegeek.enums.HotItemType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class HotItemsParams {

  @NotNull
  private HotItemType type;

}
