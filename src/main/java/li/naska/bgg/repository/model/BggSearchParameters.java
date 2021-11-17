package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectSubtype;
import li.naska.bgg.util.ToStringParamFunctions;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class BggSearchParameters {

  private String query;

  private List<ObjectSubtype> type;

  private Boolean exact;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    Optional.ofNullable(getQuery()).ifPresent(e -> map.add("query", e));
    Optional.ofNullable(getType()).ifPresent(l -> map.add("type", l.stream().map(Object::toString).collect(Collectors.joining(","))));
    Optional.ofNullable(getExact()).map(ToStringParamFunctions.BGG_BOOLEAN_FUNCTION).ifPresent(e -> map.add("exact", e));
    return map;
  }

}
