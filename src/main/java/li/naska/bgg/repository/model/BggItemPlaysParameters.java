package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectSubtype;
import com.boardgamegeek.enums.ObjectType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BggItemPlaysParameters {

  private final Integer id;

  private final ObjectType type;

  private String username;

  private LocalDate mindate;

  private LocalDate maxdate;

  private ObjectSubtype subtype;

  private Integer page;

}
