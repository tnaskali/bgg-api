package li.naska.bgg.util;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

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
