package li.naska.bgg.resource.v3.model;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class Thread {
  private String subject;
  private Integer id;
  private String link;
  private Integer numarticles;
  private List<Article> articles;

  @Data
  public static class Article {
    private String subject;
    private String body;
    private Integer id;
    private String username;
    private String link;
    private ZonedDateTime postdate;
    private ZonedDateTime editdate;
    private Integer numedits;
  }
}
