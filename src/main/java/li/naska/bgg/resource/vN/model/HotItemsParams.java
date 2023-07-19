package li.naska.bgg.resource.vN.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HotItemsParams {

  public enum HotItemType {
    boardgame,
    boardgamecompany,
    boardgameperson,
    rpg,
    rpgcompany,
    rpgperson,
    videogame,
    videogamecompany
  }

  @NotNull
  private HotItemType type;

}
