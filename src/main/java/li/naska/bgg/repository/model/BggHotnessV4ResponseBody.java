package li.naska.bgg.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class BggHotnessV4ResponseBody {

  private List<Item> items;

  @Data
  public static class Item {

    private String objecttype;

    private Integer objectid;

    private Integer rep_imageid;

    private Integer delta;

    private String href;

    private String name;

    private Integer id;

    private String type;

    private String imageurl;

    private Images images;

    private Integer yearpublished;

    private Integer rank;

    private String description;

  }

  @Data
  public static class Images {

    private Image square30;

    private Image square100;

    private Image mediacard;

  }

  @Data
  public static class Image {

    private String src;

    @JsonProperty(value = "src@2x")
    private String src_at_2x;

  }

}
