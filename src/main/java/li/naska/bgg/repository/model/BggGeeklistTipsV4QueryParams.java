package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BggGeeklistTipsV4QueryParams {

  @Min(1)
  @Max(1)
  private Integer totalonly;

}
