package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.ItemSubtype;
import com.boardgamegeek.enums.ItemType;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Plays {
  private Integer numplays;
  // paged(100)
  private List<Play> plays;

  @Data
  public static class Play {
    private Integer id;
    private String comments;
    private LocalDate date;
    private Integer quantity;
    private Integer length;
    private Boolean incomplete;
    private Boolean nowinstats;
    private String location;
    private PlayItem item;
    private List<PlayPlayer> players;

    @Data
    public static class PlayItem {
      private List<ItemSubtype> subtypes;
      private String name;
      private ItemType objecttype;
      private Integer objectid;
    }

    @Data
    public static class PlayPlayer {
      private String username;
      private Integer userid;
      private String name;
      private String startposition;
      private String color;
      private String score;
      private Boolean _new;
      private Integer rating;
      private Boolean win;
    }
  }
}
