package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.ItemType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ForumsParams {

  @NotNull
  private Integer id;

  private ItemType type;

}
