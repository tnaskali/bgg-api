package li.naska.bgg.service.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginParams {
  @NotNull
  private String username;
  @NotNull
  private String password;
}
