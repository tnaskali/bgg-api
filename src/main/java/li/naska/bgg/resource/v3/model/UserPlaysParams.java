package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.ItemSubtype;
import com.boardgamegeek.enums.ItemType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserPlaysParams {

  private Integer id;

  private ItemType type;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate mindate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate maxdate;

  private ItemSubtype subtype;

  private Integer page;

}
