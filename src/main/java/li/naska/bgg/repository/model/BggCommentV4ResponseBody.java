package li.naska.bgg.repository.model;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

@Data
public class BggCommentV4ResponseBody {
  private String type;
  private Integer id;
  private Integer commentid;
  private String body;
  private String bodyXml;
  private Source source;
  private ZonedDateTime postdate;
  private ZonedDateTime editdate;
  private Integer author;
  private String canonical_link;
  private String href;
  private Boolean collapsed;
  private List<Link> links;
  private Integer rollsCount;

  @Data
  public static class Source {
    private String type;
    private Integer id;
  }

  @Data
  public static class Link {
    private String rel;
    private String uri;
  }
}
