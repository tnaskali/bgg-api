package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class BggItemWeblinksV5ResponseBody {
  private Config config;
  private List<Weblink> weblinks;

  @Data
  @JsonIgnoreProperties(
      value = {"languages", "categories", "showcontrols"
      }) // This is only display data, no need to map to schema
  public static class Config {
    private Integer endpage;
  }

  @Data
  public static class Weblink {
    private String url;
    private String name;
    private LocalDateTime postdate;
    private Integer linkid;
    private String linktype;
    private String objecttype;
    private Integer objectid;
    private String itemstate;
    private Integer rep_imageid;
    private String objectlink;
    private List<String> languages;
    private List<String> categories;
  }
}
