package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggGeekitemsV4QueryParams {

  @NotNull
  @Min(1)
  private Integer objectid;

  @NotNull
  @Pattern(regexp = "^(company|event|family|media|person|property|thing|version)$")
  private String objecttype;

}
