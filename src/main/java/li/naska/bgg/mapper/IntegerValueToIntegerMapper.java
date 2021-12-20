package li.naska.bgg.mapper;

import com.boardgamegeek.common.IntegerValue;
import org.mapstruct.Mapper;

import java.util.Optional;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public class IntegerValueToIntegerMapper {

  public Integer asString(IntegerValue value) {
    return Optional.ofNullable(value).map(IntegerValue::getValue).orElse(null);
  }

  public IntegerValue asStringValue(Integer value) {
    return Optional.ofNullable(value).map(s -> {
      IntegerValue result = new IntegerValue();
      result.setValue(value);
      return result;
    }).orElse(null);
  }

}
