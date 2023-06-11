package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BggGeeklistsV4QueryParams {

  @NotNull
  @Min(1)
  private Integer objectid;

  @NotNull
  @Pattern(regexp = "^(company|event|family|media|person|property|thing|version)$")
  private String objecttype;

  @Min(1)
  @Max(50)
  private Integer showcount;

  @Min(1)
  private Integer pageid;

  @Min(1)
  @Max(50)
  private Integer itemcount;

}
