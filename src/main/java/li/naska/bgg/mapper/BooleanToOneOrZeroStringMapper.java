package li.naska.bgg.mapper;

import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public class BooleanToOneOrZeroStringMapper {

  public String asString(Boolean booleanValue) {
    return Optional.ofNullable(booleanValue).map(v -> v ? "1" : "0").orElse(null);
  }

  public Boolean asBoolean(String stringValue) {
    return Optional.ofNullable(stringValue).map(s -> {
      if ("1".equals(s)) {
        return true;
      } else if ("0".equals(s)) {
        return false;
      } else {
        throw new IllegalArgumentException(String.format("%s must be either '0' or '1'", stringValue));
      }
    }).orElse(null);
  }

}
