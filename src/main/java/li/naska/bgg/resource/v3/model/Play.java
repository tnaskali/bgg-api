package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.ItemSubtype;
import com.boardgamegeek.enums.ItemType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Play {
  private Integer id;
  private LocalDate date;
  private String comments;
  private Integer quantity;
  private Integer length;
  private String location;
  private Boolean incomplete;
  private Boolean nowinstats;
  private Item item;
  private List<Player> players;

  @Data
  public static class Item {
    private Integer objectid;
    private ItemType objecttype;
    private String name;
    private List<ItemSubtype> subtypes;
  }

  @Data
  public static class Player {
    private String username;
    private Integer userid;
    private String name;
    private String position;
    private String color;
    private String score;
    private Integer rating;
    private Boolean win;
    private Boolean _new;
  }
}

