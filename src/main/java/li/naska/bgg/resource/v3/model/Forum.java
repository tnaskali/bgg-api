package li.naska.bgg.resource.v3.model;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class Forum {
  private Integer id;
  private Integer groupid;
  private String title;
  private Boolean noposting;
  private String description;
  private Integer numthreads;
  // paged (50)
  private List<Thread> threads;
  private Integer numposts;
  private ZonedDateTime lastpostdate;
}
