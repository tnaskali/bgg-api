package li.naska.bgg.resource.v3.model;

import lombok.Data;

import java.util.List;

@Data
public class Plays {
  private Integer numplays;
  // paged(100)
  private List<Play> plays;

}
