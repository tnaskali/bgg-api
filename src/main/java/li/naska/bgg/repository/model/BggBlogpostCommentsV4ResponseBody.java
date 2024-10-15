package li.naska.bgg.repository.model;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

@Data
public class BggBlogpostCommentsV4ResponseBody {
  private Source selectedBy;
  private List<Comment> comments;
  private List<Paging> links;
  private Boolean locked;
  // metaonly=1
  private Integer totalCount;
  private ZonedDateTime lastPostdate;
  private List<Integer> recentCommenters;

  @Data
  public static class Source {
    private String type;
    private Integer id;
  }

  @Data
  public static class Comment {
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
  }

  @Data
  public static class Link {
    private String rel;
    private String uri;
  }

  @Data
  public static class Paging {
    private String rel;
    private String uri;
    private Meta meta;
  }

  @Data
  public static class Meta {
    private Integer totalCount;
  }
}
