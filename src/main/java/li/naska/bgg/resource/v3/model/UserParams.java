package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.UserDomainType;
import lombok.Data;

@Data
public class UserParams {

  private Boolean buddies;

  private Boolean guilds;

  private Boolean hot;

  private Boolean top;

  private UserDomainType domain;

  private Integer page;

}
