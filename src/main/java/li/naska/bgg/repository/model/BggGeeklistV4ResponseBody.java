package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

@Data
public class BggGeeklistV4ResponseBody {
  private String type;
  private Integer id;
  private String name;
  private Integer creator;
  private String href;
  private String canonical_link;
  private ZonedDateTime postdate;
  private ZonedDateTime editdate;
  private String body;
  private String bodyXml;

  @JsonProperty(value = "private")
  private Boolean _private;

  private Boolean publicAdditionsAllowed;
  private Boolean commentsAllowed;
  private Boolean trade;
  private String ordinalDirection;
  private Boolean submitted;
  private String sortType;
  private List<String> domains;
  private List<Link> links;

  @Data
  public static class Link {
    private String rel;
    private String uri;
  }
}
