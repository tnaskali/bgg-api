package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(value = "crumbs")
public class BggThreadV4ResponseBody {

  private String type;

  private Integer id;

  private Source source;

  private Integer forumid;

  private Integer forumuid;

  private Boolean locked;

  private Boolean pinned;

  private Boolean frontpinned;

  private Boolean hidefrontpage;

  private Boolean hidden;

  private Integer threadid;

  private Integer shadowthreadid;

  private String subject;

  private ZonedDateTime postdate;

  private ZonedDateTime editdate;

  private Integer creator;

  private Integer numposts;

  private Integer pagesize;

  private Integer numpages;

  private String href;

  private Boolean blocks_ads;

  private String canonical_link;

  private List<Link> links;

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
