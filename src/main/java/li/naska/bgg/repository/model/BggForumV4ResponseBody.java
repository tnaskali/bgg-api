package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = {"hasAngularLink", "descriptors"})
public class BggForumV4ResponseBody {
  private String type;
  private Long id;
  private String name;
  private String href;
  private String label;
  private String labelpl;
  private Boolean hasAngularLink;
  private List<Breadcrumb> breadcrumbs;
  private ImageSets imageSets;

  @Data
  @JsonIgnoreProperties(value = {"hasAngularLink"})
  private static class Breadcrumb {
    private String name;
    private String href;
  }

  @Data
  public static class ImageSets {
    private Image square;
    private Image square100;
    private Image mediacard;
    private Image mediacard100;
  }

  @Data
  public static class Image {
    private String src;

    @JsonProperty(value = "src@2x")
    private String src_at_2x;
  }
}
