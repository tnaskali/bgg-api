package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.FamilyLinkType;
import com.boardgamegeek.enums.FamilyType;
import lombok.Data;

import java.util.List;

@Data
public class Family {
  private Integer id;
  private FamilyType type;
  private String name;
  private List<String> alternateNames;
  private String description;
  private String thumbnail;
  private String image;
  private List<FamilyLink> links;

  @Data
  public static class FamilyLink {
    private Integer id;
    private FamilyLinkType type;
    private String value;
    private Boolean inbound;
  }
}
