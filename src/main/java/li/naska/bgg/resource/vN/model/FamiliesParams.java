package li.naska.bgg.resource.vN.model;

import com.boardgamegeek.enums.FamilyType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class FamiliesParams {

  @NotNull
  private List<Integer> id;

  private List<FamilyType> type;

}
