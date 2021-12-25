package li.naska.bgg.resource.v3.model;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class Guild {
  private Integer id;
  private String name;
  private ZonedDateTime created;
  private String category;
  private String website;
  private String manager;
  private String description;
  private Location location;
  private Integer nummembers;
  // paged (25)
  private List<Member> members;

  @Data
  public static class Location {
    private String addr1;
    private String addr2;
    private String city;
    private String stateorprovince;
    private String postalcode;
    private String country;
  }

  @Data
  public static class Member {
    private String name;
    private ZonedDateTime date;
  }
}
