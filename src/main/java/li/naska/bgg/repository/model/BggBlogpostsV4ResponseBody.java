package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class BggBlogpostsV4ResponseBody {
  private List<Blogpost> data;

  @Data
  @JsonIgnoreProperties(value = "hasAngularLink")
  private static class Blogpost {
    private String type;
    private Integer id;
    private String href;
    private String name;
    private Integer numcomments;
    private BlogItem creator;
    private BlogItem parent;
    private Integer numthumbs;
    private Reactions reactions;
    private Integer numreplies;
    private ImageSets imageSets;

    @Data
    @JsonIgnoreProperties(value = {"hasAngularLink", "breadcrumbs"})
    private static class BlogItem {
      private String type;
      private Integer id;
      private String name;
      private String href;
      private String label;
      private String labelpl;
      private List<Descriptor> descriptors;

      @Data
      public static class Descriptor {
        private String name;
        private String displayValue;
      }
    }

    @Data
    private static class Reactions {
      private Integer thumbs;
    }

    @Data
    public static class ImageSets {
      private Image square;
      private Image square100;
      private Image mediacard;
      private Image mediacard100;

      @Data
      public static class Image {
        private String src;

        @JsonProperty(value = "src@2x")
        private String src_at_2x;
      }
    }
  }
}
