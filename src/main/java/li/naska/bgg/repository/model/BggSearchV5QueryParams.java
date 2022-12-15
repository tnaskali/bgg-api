package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BggSearchV5QueryParams {

  @NotNull
  private String q;

  @NotNull
  @Min(1)
  @Max(100)
  private Integer showcount;

}
