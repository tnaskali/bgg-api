package li.naska.bgg.repository.model;

import li.naska.bgg.util.ToStringParamFunctions;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Data
public class BggThreadParameters {

  private Integer id;

  private Integer minarticleid;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate minarticledate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  private LocalTime minarticletime;

  private Integer count;

  public MultiValueMap<String, String> toMultiValueMap() {
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    Optional.ofNullable(getId()).ifPresent(e -> map.add("id", e.toString()));
    Optional.ofNullable(getMinarticleid()).ifPresent(e -> map.add("minarticleid", e.toString()));
    if (getMinarticledate() != null) {
      if (getMinarticletime() != null) {
        map.add("minarticledate", ToStringParamFunctions.BGG_LOCALDATETIME_FUNCTION.apply(getMinarticledate().atTime(getMinarticletime())));
      } else {
        map.add("minarticledate", ToStringParamFunctions.BGG_LOCALDATE_FUNCTION.apply(getMinarticledate()));
      }
    }
    Optional.ofNullable(getCount()).ifPresent(e -> map.add("count", e.toString()));
    // BGG API: not currently supported
    // Optional.ofNullable(getUsername()).ifPresent(e -> map.add("username", e));
    return map;
  }

}
