package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectSubtype;
import lombok.Data;

import java.util.List;

@Data
public class BggSearchParameters {

  private final String query;

  private List<ObjectSubtype> types;

  private Boolean exact;

}
