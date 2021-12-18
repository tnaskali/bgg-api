package li.naska.bgg.resource.v3.model;

import com.boardgamegeek.enums.UserDomainType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserParams {

  @NotNull
  private String username;

  private Boolean buddies;

  private Boolean guilds;

  private Boolean hot;

  private Boolean top;

  private UserDomainType domain;

  private Integer page;

}
