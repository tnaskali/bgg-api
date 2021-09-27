package li.naska.bgg.repository.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BggThreadsParameters {

  private final Integer id;

  private Integer minarticleid;

  private LocalDate minarticledate;

  private LocalTime minarticletime;

  private Integer count;

}
