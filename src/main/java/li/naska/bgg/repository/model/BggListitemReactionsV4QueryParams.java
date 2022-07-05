package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class BggListitemReactionsV4QueryParams {

  @Min(1)
  @Max(1)
  private Integer totalonly;

}
