package li.naska.bgg.service.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginParams {

  @NotNull
  private String username;

  @NotNull
  private String password;

}
