package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(value = {"itemdata", "linkdata"}) // This is only display data, no need to map to schema
public class BggGeekitemLinkeditemsV4ResponseBody {

  private List<Item> items;

  private Config config;

  @Data
  public static class Item {

    private Integer usersrated;

    private BigDecimal average;

    private BigDecimal avgweight;

    private Integer numowned;

    private Integer numprevowned;

    private Integer numtrading;

    private Integer numwanting;

    private Integer numwish;

    private Integer numcomments;

    private Integer yearpublished;

    private Integer rank;

    private String name;

    private LocalDateTime postdate;

    private Integer linkid;

    private String linktype;

    private String objecttype;

    private Integer objectid;

    private String itemstate;

    private Integer rep_imageid;

    private String subtype;

    private List<Object> links;

    private String href;

    private Images images;

  }

  @Data
  public static class Images {

    private String thumb;

    private String micro;

    private String square;

    private String squarefit;

    private String tallthumb;

    private String previewthumb;

    private String square200;

  }

  @Data
  @JsonIgnoreProperties(value = {"sortdata", "filters"}) // This is only display data, no need to map to schema
  public static class Config {

    private Integer numitems;

  }

}
