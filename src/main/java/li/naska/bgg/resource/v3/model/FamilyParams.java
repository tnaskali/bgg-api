package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.FamilyType;
import lombok.Data;

import java.util.List;

@Data
public class FamilyParams {

  private List<FamilyType> type;

}
