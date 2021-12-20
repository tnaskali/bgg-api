package li.naska.bgg.mapper;

import com.boardgamegeek.common.StringValue;
import org.mapstruct.Mapper;

import java.util.Optional;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public class StringValueToStringMapper {

  public String asString(StringValue value) {
    return Optional.ofNullable(value).map(StringValue::getValue).orElse(null);
  }

  public StringValue asStringValue(String value) {
    return Optional.ofNullable(value).map(s -> {
      StringValue result = new StringValue();
      result.setValue(value);
      return result;
    }).orElse(null);
  }

}
