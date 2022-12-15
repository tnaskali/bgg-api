package li.naska.bgg.resource.vN.model;

import com.boardgamegeek.enums.ThingType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ThingsParams {

  @NotNull
  private List<Integer> ids;

  private List<ThingType> types;

  private Boolean stats;

}
