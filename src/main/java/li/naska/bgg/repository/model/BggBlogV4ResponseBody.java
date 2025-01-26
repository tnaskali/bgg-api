package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BggBlogV4ResponseBody {
  private String type;
  private Integer id;
  private String name;
  private String href;
  private Integer postCount;
  private Integer subscriptionCount;
  private String canonical_link;
  private String description;
  private Images images;

  @Data
  public static class Images {
    private Image logo;
    private Image banner;

    @Data
    public static class Image {
      private String src;

      @JsonProperty(value = "src@2x")
      private String src_at_2x;
    }
  }
}
