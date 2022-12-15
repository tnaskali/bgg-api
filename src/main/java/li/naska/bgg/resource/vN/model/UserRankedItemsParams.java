package li.naska.bgg.resource.vN.model;

import com.boardgamegeek.enums.UserDomainType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRankedItemsParams {

  @NotNull
  private UserDomainType domain;

}
