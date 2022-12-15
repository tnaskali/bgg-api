package li.naska.bgg.resource.vN.model;

import com.boardgamegeek.enums.HotItemType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HotItemsParams {

  @NotNull
  private HotItemType type;

}
