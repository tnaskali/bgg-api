package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectSubtype;
import li.naska.bgg.util.ToStringParamFunctions;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

@Data
public class BggSearchParameters {

  private final String query;

  private List<ObjectSubtype> types;

  private Boolean exact;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("query", getQuery());
    Optional.ofNullable(getTypes()).map(ToStringParamFunctions.BGG_OBJECT_SUBTYPE_LIST_FUNCTION).ifPresent(e -> map.set("type", e));
    Optional.ofNullable(getExact()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.set("exact", e));
    return map;
  }

}
