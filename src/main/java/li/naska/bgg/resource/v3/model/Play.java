package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.ItemType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Play {
  private Integer id;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate date;
  private String comments;
  private Integer durationInMinutes;
  private String location;
  private ItemType objectType;
  private Integer objectId;
  private Integer numberOfPlays;
  private List<Player> players;
  private Boolean unfinished;
  private Boolean noWinStats;

  @Data
  public static class Player {
    private String bggUsername;
    private String name;
    private String startingPosition;
    private String color;
    private String score;
    private String rating;
    private Boolean won;
    private Boolean firstTimePlayer;
  }
}
