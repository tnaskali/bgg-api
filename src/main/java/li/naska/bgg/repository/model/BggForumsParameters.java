package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectType;
import lombok.Data;

@Data
public class BggForumsParameters {

  private final Integer id;

  private final ObjectType type;

}
