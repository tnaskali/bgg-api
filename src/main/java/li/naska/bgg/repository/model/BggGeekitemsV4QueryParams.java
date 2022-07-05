package li.naska.bgg.repository.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class BggGeekitemsV4QueryParams {

  @NotNull
  @Min(1)
  private Integer objectid;

  @NotNull
  @Pattern(regexp = "^(company|event|family|media|person|property|thing)")
  private String objecttype;

}
