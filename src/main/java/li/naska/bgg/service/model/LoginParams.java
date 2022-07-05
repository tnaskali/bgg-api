package li.naska.bgg.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class LoginParams {
  @NotNull
  private String username;
  @NotNull
  private String password;
}
