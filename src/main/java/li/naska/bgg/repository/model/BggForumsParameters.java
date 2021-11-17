package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectType;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@Data
public class BggForumsParameters {

  private Integer id;

  private ObjectType type;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    Optional.ofNullable(getId()).ifPresent(e -> map.add("id", e.toString()));
    Optional.ofNullable(getType()).ifPresent(e -> map.add("type", e.toString()));
    return map;
  }

}
