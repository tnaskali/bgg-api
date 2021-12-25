package li.naska.bgg.mapper;

import com.boardgamegeek.common.LocalDateValue;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.util.Optional;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public class LocalDateValueToLocalDateMapper {

  public LocalDate asLocalDate(LocalDateValue value) {
    return Optional.ofNullable(value).map(LocalDateValue::getValue).orElse(null);
  }

  public LocalDateValue asLocalDateValue(LocalDate value) {
    return Optional.ofNullable(value).map(s -> {
      LocalDateValue result = new LocalDateValue();
      result.setValue(value);
      return result;
    }).orElse(null);
  }

}
