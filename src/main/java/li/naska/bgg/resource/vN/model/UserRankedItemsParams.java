package li.naska.bgg.resource.vN.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRankedItemsParams {

  @NotNull
  private String domain;

}
