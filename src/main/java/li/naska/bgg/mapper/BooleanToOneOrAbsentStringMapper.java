package li.naska.bgg.mapper;

import org.mapstruct.Mapper;

import java.util.Optional;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public class BooleanToOneOrAbsentStringMapper {

  public String asString(Boolean booleanValue) {
    return Optional.ofNullable(booleanValue).map(v -> v ? "1" : null).orElse(null);
  }

  public Boolean asBoolean(String stringValue) {
    return Optional.ofNullable(stringValue).map(s -> {
      if ("1".equals(s)) {
        return true;
      } else {
        throw new IllegalArgumentException(String.format("%s must be '1'", stringValue));
      }
    }).orElse(false);
  }

}
