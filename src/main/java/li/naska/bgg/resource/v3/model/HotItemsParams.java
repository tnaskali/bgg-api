package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.HotItemType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class HotItemsParams {

  @NotNull
  private HotItemType type;

}
