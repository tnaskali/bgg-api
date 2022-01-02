package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.ThingType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ThingsParams {

  @NotNull
  private List<Integer> ids;

  private List<ThingType> types;

  private Boolean stats;

}
