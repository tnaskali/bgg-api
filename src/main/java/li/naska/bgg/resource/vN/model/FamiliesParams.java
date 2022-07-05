package li.naska.bgg.resource.vN.model;

import com.boardgamegeek.enums.FamilyType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FamiliesParams {

  @NotNull
  private List<Integer> id;

  private List<FamilyType> type;

}
