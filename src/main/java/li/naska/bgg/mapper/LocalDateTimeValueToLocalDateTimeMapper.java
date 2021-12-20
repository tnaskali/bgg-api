package li.naska.bgg.mapper;

import com.boardgamegeek.common.LocalDateTimeValue;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public class LocalDateTimeValueToLocalDateTimeMapper {

  public LocalDateTime asLocalDateTime(LocalDateTimeValue value) {
    return Optional.ofNullable(value).map(LocalDateTimeValue::getValue).orElse(null);
  }

  public LocalDateTimeValue asLocalDateTimeValue(LocalDateTime value) {
    return Optional.ofNullable(value).map(s -> {
      LocalDateTimeValue result = new LocalDateTimeValue();
      result.setValue(value);
      return result;
    }).orElse(null);
  }

}
