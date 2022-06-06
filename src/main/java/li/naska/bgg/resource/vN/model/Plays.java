package li.naska.bgg.resource.vN.model;

import lombok.Data;

import java.util.List;

@Data
public class Plays {
  private Integer numplays;
  // paged(100)
  private List<Play> plays;

}
