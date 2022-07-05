package li.naska.bgg.resource.vN.model;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class Thread {
  private Integer id;
  private String subject;
  private String author;
  private String link;
  private ZonedDateTime postdate;
  private ZonedDateTime lastpostdate;
  private Integer numarticles;
  private List<Article> articles;

  @Data
  public static class Article {
    private Integer id;
    private String subject;
    private String body;
    private String username;
    private String link;
    private ZonedDateTime postdate;
    private ZonedDateTime editdate;
    private Integer numedits;
  }
}
