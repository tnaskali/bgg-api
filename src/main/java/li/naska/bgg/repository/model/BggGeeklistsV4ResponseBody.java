package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class BggGeeklistsV4ResponseBody {
  private List<Geeklist> lists;
  private Config config;

  @Data
  public static class Geeklist {
    private String href;
    private Integer numrecommend;
    private String username;
    private Integer numitems;
    private String title;
    private LocalDateTime postdate;
    private LocalDateTime lastreplydate;
    private List<Item> items;
  }

  @Data
  public static class Item {
    private Integer itemid;
    private Integer objectid;
    private String objecttype;
    private String name;
    private String imageurl;
  }

  @Data
  @JsonIgnoreProperties(
      value = {"categoryfilters"}) // This is only display data, no need to map to schema
  public static class Config {
    private Integer numitems;
    private Integer endpage;
  }
}
