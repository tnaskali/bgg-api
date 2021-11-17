package li.naska.bgg.repository.model;

import com.boardgamegeek.enums.ObjectSubtype;
import com.boardgamegeek.enums.ObjectType;
import li.naska.bgg.util.ToStringParamFunctions;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.Optional;

@Data
public class BggPlaysParameters {

  private String username;

  private Integer id;

  private ObjectType type;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate mindate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate maxdate;

  private ObjectSubtype subtype;

  private Integer page;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    Optional.ofNullable(getUsername()).ifPresent(e -> map.add("username", e));
    Optional.ofNullable(getId()).ifPresent(e -> map.add("id", e.toString()));
    Optional.ofNullable(getType()).ifPresent(e -> map.add("type", e.toString()));
    Optional.ofNullable(getMindate()).map(ToStringParamFunctions.BGG_LOCALDATE_FUNCTION).ifPresent(e -> map.add("mindate", e));
    Optional.ofNullable(getMaxdate()).map(ToStringParamFunctions.BGG_LOCALDATE_FUNCTION).ifPresent(e -> map.add("maxdate", e));
    Optional.ofNullable(getSubtype()).ifPresent(e -> map.add("subtype", e.toString()));
    Optional.ofNullable(getPage()).ifPresent(e -> map.add("page", e.toString()));
    return map;
  }

}
