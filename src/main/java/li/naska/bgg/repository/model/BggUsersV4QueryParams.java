package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BggUsersV4QueryParams {

  @NotNull
  private String username;

}
