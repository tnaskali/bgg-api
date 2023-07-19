package li.naska.bgg.resource.vN.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserPlaysParams {

  private Integer id;

  private String type;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate mindate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate maxdate;

  private String subtype;

}
