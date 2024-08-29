package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class BggGeekitemsV4ResponseBody {

  private Item item;

  @Data
  public static class Item {

    private Integer itemid;

    private String objecttype;

    private Integer objectid;

    private String label;

    private String labelpl;

    private String shortlabel;

    private String shortlabelpl;

    private String type;

    private Integer id;

    private String href;

    private String subtype;

    private List<String> subtypes;

    private VersionInfo versioninfo;

    private String name;

    private String alternatename;

    private Integer yearpublished;
    private Integer minplayers;
    private Integer maxplayers;

    // --- type specific properties
    // type=thing,subtype=bgsleeve
    private Integer mfgid;
    // type=thing,subtype=boardgame
    private Integer minplaytime;
    private Integer maxplaytime;
    private Integer minage;
    private Integer override_rankable;
    private String targetco_url;
    private Integer walmart_id;
    private Integer instructional_videoid;
    private Integer summary_videoid;
    private Integer playthrough_videoid;
    private Integer focus_videoid;
    private Integer howtoplay_videoid;
    private String bggstore_product;
    private String short_description;
    // type=thing,subtype=rpgitem
    private String seriescode;
    // type=thing,subtype=videogame
    private LocalDate releasedate;
    // ---

    private Map<String, List<LinkedItem>> links;

    private Map<String, Integer> linkcounts;

    private List<CommerceLink> commercelinks;

    // --- type specific properties
    // type=thing,subtype=bgsleeve
    private Integer quantity;
    private String quality;
    private String finish;
    private Integer width;
    private Integer height;
    private BigDecimal max_width;
    private BigDecimal max_height;
    private Integer thickness;
    private String package_color_name;
    private String package_color_hexcode;
    private Integer rep_imageid;
    // ---

    private Integer secondarynamescount;

    private Integer alternatenamescount;

    private Name primaryname;

    private List<AlternateName> alternatenames;

    private String description;

    private String wiki;

    private Website website;

    private Integer imageid;

    private Images images;

    private String imagepagehref;

    private String imageurl;

    private String topimageurl;

    private ImageSets imageSets;

    private String itemstate;

    /* TODO unknown format */
    private Object promoted_ad;

    private User special_user;
  }

  @Data
  public static class VersionInfo {

    private String gamepageorderurl;

    /* TODO unknown format */
    private Object shopifyitem;
  }

  @Data
  public static class LinkedItem {

    private String name;

    private Integer sortindex;

    private String objecttype;

    private Integer objectid;

    private Integer primarylink;

    private String itemstate;

    private String href;
  }

  @Data
  public static class CommerceLink {

    private LocalDate orderstartdate;

    private LocalDate orderenddate;

    private String ordertype;

    private String commerceurl;

    private String rect_ad_url;

    private String seller_name;

    private String premium;

    /* TODO unknown format */
    private Object shopifyitem;
  }

  @Data
  public static class Name {

    private Integer nameid;

    private String name;

    private Integer sortindex;

    private Integer primaryname;

    private String translit;
  }

  @Data
  public static class AlternateName {

    private Integer nameid;

    private String name;

    private Boolean secondaryname;
  }

  @Data
  public static class Website {

    /**
     * can be either Boolean ("false" if absent) or String (if present)
     */
    private Object url;

    private String title;
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

    private String original;
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
  public static class User {

    private String username;

    private Integer userid;
  }
}
