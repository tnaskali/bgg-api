package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class BggGeekitemRecsV4ResponseBody {
  private List<Rec> recs;
  private Integer numrecs;

  @Data
  public static class Rec {
    private String description;
    private Item item;
    private Image image;
    private BigDecimal rating;
    private Integer numvoters;
    private Integer rank;
    private Integer yearpublished;
    private Integer numfans;
    private Image topImage;

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

      @Data
      public static class Descriptor {
        private String name;
        private String displayValue;
      }

      @Data
      public static class ImageSets {
        private Image square;
        private Image square100;
        private Image mediacard;
        private Image mediacard100;
      }
    }

    @Data
    public static class Image {
      private String src;

      @JsonProperty(value = "src@2x")
      private String src_at_2x;
    }
  }
}
