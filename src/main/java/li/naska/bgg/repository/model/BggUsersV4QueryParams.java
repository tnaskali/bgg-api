package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BggUsersV4QueryParams {

  @NotNull
  @Parameter(
      example = "Jester",
      description = """
          Username.
          <p>
          <i>Syntax</i> : /users?username={username}
          <p>
          <i>Example</i> : /users?username=Jester
          """
  )
  private String username;

}
