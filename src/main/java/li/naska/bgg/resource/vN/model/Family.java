package li.naska.bgg.resource.vN.model;

import lombok.Data;

import java.util.List;

@Data
public class Family {
  private Integer id;
  private String type;
  private Name name;
  private List<Name> alternatenames;
  private String description;
  private String thumbnail;
  private String image;
  private List<Link> links;

  @Data
  public static class Link {
    private Integer id;
    private String type;
    private String value;
    private Boolean inbound;
  }
}
