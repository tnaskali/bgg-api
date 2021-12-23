package li.naska.bgg.resource.v3.model;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class Forum {
  private Integer id;
  private String title;
  private Integer numposts;
  private ZonedDateTime lastpostdate;
  private Integer noposting;
  private Integer numthreads;
  // paged (50)
  private List<ForumThread> threads;

  @Data
  public static class ForumThread {
    private Integer id;
    private String subject;
    private String author;
    private Integer numarticles;
    private ZonedDateTime postdate;
    private ZonedDateTime lastpostdate;
  }
}
