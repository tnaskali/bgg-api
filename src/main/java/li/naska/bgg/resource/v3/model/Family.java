package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.FamilyLinkType;
import com.boardgamegeek.enums.FamilyType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
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
  @JsonInclude(NON_NULL)
  public static class FamilyLink {
    private Integer id;
    private FamilyLinkType type;
    private String value;
    private LinkDirection direction;
  }
}
