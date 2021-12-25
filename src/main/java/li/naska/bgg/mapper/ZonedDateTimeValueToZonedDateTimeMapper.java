package li.naska.bgg.mapper;

import com.boardgamegeek.common.ZonedDateTimeValue;
import org.mapstruct.Mapper;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public class ZonedDateTimeValueToZonedDateTimeMapper {

  public ZonedDateTime asZonedDateTime(ZonedDateTimeValue value) {
    return Optional.ofNullable(value).map(ZonedDateTimeValue::getValue).orElse(null);
  }

  public ZonedDateTimeValue asZonedDateTimeValue(ZonedDateTime value) {
    return Optional.ofNullable(value).map(s -> {
      ZonedDateTimeValue result = new ZonedDateTimeValue();
      result.setValue(value);
      return result;
    }).orElse(null);
  }

}
