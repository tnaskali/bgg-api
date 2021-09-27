package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectSubtype;
import com.boardgamegeek.enums.ObjectType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BggUserPlaysParameters {

  private final String username;

  private Integer id;

  private ObjectType type;

  private LocalDate mindate;

  private LocalDate maxdate;

  private ObjectSubtype subtype;

  private Integer page;

}
