package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class BggUnifiedSearchQueryParams {

  @NotNull
  private String q;

  @NotNull
  @Min(1)
  @Max(100)
  private Integer showcount;

}
