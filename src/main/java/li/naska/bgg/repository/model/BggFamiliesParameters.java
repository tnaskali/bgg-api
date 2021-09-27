package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.FamilyType;
import lombok.Data;

import java.util.List;

@Data
public class BggFamiliesParameters {

  private final Integer id;

  private List<FamilyType> types;

}
