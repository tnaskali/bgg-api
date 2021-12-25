package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.ItemSubtype;
import com.boardgamegeek.enums.ItemType;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class Geeklist {
  private Integer id;
  private String title;
  private String description;
  private ZonedDateTime postdate;
  private Integer postdate_timestamp;
  private ZonedDateTime editdate;
  private Integer editdate_timestamp;
  private Integer thumbs;
  private Integer numitems;
  private String username;
  private List<Comment> comments;
  private List<Item> items;

  @Data
  public static class Comment {
    private String value;
    private String username;
    private ZonedDateTime date;
    private ZonedDateTime postdate;
    private ZonedDateTime editdate;
    private Integer thumbs;
  }

  @Data
  public static class Item {
    private Integer id;
    private Integer objectid;
    private String objectname;
    private ItemType objecttype;
    private ItemSubtype subtype;
    private String body;
    private List<Comment> comments;
    private String username;
    private ZonedDateTime postdate;
    private ZonedDateTime editdate;
    private Integer thumbs;
    private Integer imageid;
  }
}
