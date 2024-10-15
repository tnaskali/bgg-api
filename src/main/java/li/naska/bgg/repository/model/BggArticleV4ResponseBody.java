package li.naska.bgg.repository.model;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

@Data
public class BggArticleV4ResponseBody {
  private String type;
  private Integer id;
  private Source source;
  private Boolean firstPost;
  private String href;
  private String canonical_link;
  private Boolean collapsed;
  private Boolean rollsEnabled;
  private AdminStatus adminStatus;
  private List<Link> links;
  private Integer author;
  private ZonedDateTime postdate;
  private ZonedDateTime editdate;
  private String body;
  private String bodyXml;
  private Integer rollsCount;
  private Errors errors;

  @Data
  public static class Source {
    private String type;
    private Integer id;
  }

  @Data
  public static class AdminStatus {
    private String visibility;
    private String stub;
  }

  @Data
  public static class Link {
    private String rel;
    private String uri;
  }

  @Data
  public static class Errors {
    private String error;
  }
}
