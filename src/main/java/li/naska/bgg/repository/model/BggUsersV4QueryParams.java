package li.naska.bgg.repository.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BggUsersV4QueryParams {

  @NotNull
  private String username;

}
