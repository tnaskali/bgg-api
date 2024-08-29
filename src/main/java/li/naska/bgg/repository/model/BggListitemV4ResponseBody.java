package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

@Data
public class BggListitemV4ResponseBody {

  private String type;

  private Integer id;

  private Integer listid;

  private Item item;

  private ZonedDateTime postdate;

  private ZonedDateTime editdate;

  private String body;

  private String bodyXml;

  private Integer author;

  private String href;

  private Integer imageid;

  private Boolean imageOverridden;

  private ImageLink linkedImage;

  private Boolean rollsEnabled;

  private List<Link> links;

  private Integer rollsCount;

  private Stats stats;

  @Data
  @JsonIgnoreProperties(value = {"hasAngularLink", "breadcrumbs"})
  public static class Item {

    private String type;

    private Integer id;

    private String name;

    private String href;

    private String label;

    private String labelpl;

    private List<Descriptor> descriptors;

    private ImageSets imageSets;

    private Integer imageid;

    private Integer nameSortIndex;
  }

  @Data
  public static class Descriptor {

    private String name;

    private String displayValue;
  }

  @Data
  public static class ImageSets {

    private Image square100;

    private Image mediacard;
  }

  @Data
  public static class Image {

    private String src;

    @JsonProperty(value = "src@2x")
    private String src_at_2x;
  }

  @Data
  public static class Link {

    private String rel;

    private String uri;
  }

  @Data
  public static class ImageLink {

    private String href;

    private String alt;

    private Image image;

    private Boolean blocks_ads;
  }

  @Data
  public static class Stats {

    private BigDecimal average;

    private Integer rank;
  }
}
