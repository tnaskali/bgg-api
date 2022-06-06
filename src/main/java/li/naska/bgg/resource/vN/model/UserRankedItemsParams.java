package li.naska.bgg.resource.vN.model;

import com.boardgamegeek.enums.UserDomainType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRankedItemsParams {

  @NotNull
  private UserDomainType domain;

}
