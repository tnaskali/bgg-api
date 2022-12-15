package li.naska.bgg.repository.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BggArticlesV4QueryParams {

  @NotNull
  @Min(1)
  private Integer threadid;

}
