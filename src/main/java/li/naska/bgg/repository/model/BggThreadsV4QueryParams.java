package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class BggThreadsV4QueryParams {

  @NotNull
  @Min(1)
  private Integer objectid;

  @NotNull
  @Pattern(regexp = "^(thing|family)$")
  private String objecttype;

  @Min(1)
  private Integer pageid;

  @Min(1)
  @Max(50)
  private Integer showcount;

}
