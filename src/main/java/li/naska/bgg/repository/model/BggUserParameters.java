package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.DomainType;
import lombok.Data;

@Data
public class BggUserParameters {

  private final String username;

  private Boolean buddies;

  private Boolean guilds;

  private Boolean hot;

  private Boolean top;

  private DomainType domain;

  private Integer page;

}
