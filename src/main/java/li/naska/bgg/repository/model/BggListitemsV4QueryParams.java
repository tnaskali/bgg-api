package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class BggListitemsV4QueryParams {

  @NotNull
  @Min(1)
  private Integer listid;

}
