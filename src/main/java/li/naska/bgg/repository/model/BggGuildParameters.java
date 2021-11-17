package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.SortType;
import li.naska.bgg.util.ToStringParamFunctions;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@Data
public class BggGuildParameters {

  private Integer id;

  private Boolean members;

  private SortType sort;

  private Integer page;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    Optional.ofNullable(getId()).ifPresent(e -> map.add("id", e.toString()));
    Optional.ofNullable(getMembers()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("members", e));
    Optional.ofNullable(getSort()).ifPresent(e -> map.add("sort", e.toString()));
    Optional.ofNullable(getPage()).ifPresent(e -> map.add("page", e.toString()));
    return map;
  }

}
