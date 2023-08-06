package li.naska.bgg.repository.model;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggGeekaccountContactV3QueryParams {

  @NotNull
  @Pattern(regexp = "^(editcontact)$")
  @Parameter(
      example = "editcontact",
      description = """
          Action to perform.
          <p>
          Possible values are:
          <li/>editcontact (select)
          """
  )
  private String action;

}
