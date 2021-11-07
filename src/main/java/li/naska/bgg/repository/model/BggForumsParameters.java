package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectType;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
public class BggForumsParameters {

  private final Integer id;

  private final ObjectType type;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("id", getId().toString());
    map.set("type", getType().value());
    return map;
  }

}
