package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.SortType;
import li.naska.bgg.util.ToStringParamFunctions;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@Data
public class BggGuildParameters {

  private final Integer id;

  private Boolean members;

  private SortType sort;

  private Integer page;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("id", getId().toString());
    Optional.ofNullable(getMembers()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("members", e));
    Optional.ofNullable(getSort()).map(ToStringParamFunctions.BGG_SORT_TYPE_FUNCTION).ifPresent(e -> map.set("sort", e));
    Optional.ofNullable(getPage()).map(Object::toString).ifPresent(e -> map.set("page", e));
    return map;
  }

}
