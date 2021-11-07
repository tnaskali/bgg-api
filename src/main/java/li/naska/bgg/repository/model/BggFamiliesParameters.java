package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.FamilyType;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Data
public class BggFamiliesParameters {

  private final Integer id;

  private List<FamilyType> types;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("id", getId().toString());
    // Optional.ofNullable(params.getTypes()).map(QueryParamFunctions.BGG_FAMILY_TYPE_LIST_FUNCTION).ifPresent(e -> map.set("type", e));
    return map;
  }

}
