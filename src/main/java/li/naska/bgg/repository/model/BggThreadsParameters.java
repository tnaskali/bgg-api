package li.naska.bgg.repository.model;

import li.naska.bgg.util.ToStringParamFunctions;
import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Data
public class BggThreadsParameters {

  private final Integer id;

  private Integer minarticleid;

  private LocalDate minarticledate;

  private LocalTime minarticletime;

  private Integer count;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.set("id", getId().toString());
    Optional.ofNullable(getMinarticleid()).map(Object::toString).ifPresent(e -> map.set("minarticleid", e));
    if (getMinarticledate() != null) {
      if (getMinarticletime() != null) {
        map.set("minarticledate", ToStringParamFunctions.BGG_LOCALDATETIME_FUNCTION.apply(getMinarticledate().atTime(getMinarticletime())));
      } else {
        map.set("minarticledate", ToStringParamFunctions.BGG_LOCALDATE_FUNCTION.apply(getMinarticledate()));
      }
    }
    Optional.ofNullable(getCount()).map(Object::toString).ifPresent(e -> map.set("count", e));
    // BGG API: not currently supported
    // Optional.ofNullable(getUsername()).ifPresent(e -> map.set("username", e));
    return map;
  }

}
