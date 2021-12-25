package li.naska.bgg.resource.v3.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ThreadParams {

  private Integer minarticleid;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate minarticledate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  private LocalTime minarticletime;

  private Integer count;

}
