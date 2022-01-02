package li.naska.bgg.util;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;

@Data
public class PagingParams {

  @Min(1)
  private Integer page;

  @Min(10)
  @Max(100)
  private Integer size;

  public Integer getPage() {
    return Optional.ofNullable(page).orElse(1);
  }

  public Integer getSize() {
    return Optional.ofNullable(size).orElse(100);
  }

}
