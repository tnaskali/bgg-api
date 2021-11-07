package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectSubtype;
import com.boardgamegeek.enums.ObjectType;
import li.naska.bgg.util.ToStringParamFunctions;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.Optional;

@Data
public class BggUserPlaysParameters {

  private final String username;

  private Integer id;

  private ObjectType type;

  private LocalDate mindate;

  private LocalDate maxdate;

  private ObjectSubtype subtype;

  private Integer page;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("username", getUsername());
    Optional.ofNullable(getId()).map(Object::toString).ifPresent(e -> map.set("id", e));
    Optional.ofNullable(getType()).map(ToStringParamFunctions.BGG_OBJECT_TYPE_FUNCTION).ifPresent(e -> map.set("type", e));
    Optional.ofNullable(getMindate()).map(ToStringParamFunctions.BGG_LOCALDATE_FUNCTION).ifPresent(e -> map.set("mindate", e));
    Optional.ofNullable(getMaxdate()).map(ToStringParamFunctions.BGG_LOCALDATE_FUNCTION).ifPresent(e -> map.set("maxdate", e));
    Optional.ofNullable(getSubtype()).map(ToStringParamFunctions.BGG_OBJECT_SUBTYPE_FUNCTION).ifPresent(e -> map.set("subtype", e));
    Optional.ofNullable(getPage()).map(Object::toString).ifPresent(e -> map.set("page", e));
    return map;
  }

}
