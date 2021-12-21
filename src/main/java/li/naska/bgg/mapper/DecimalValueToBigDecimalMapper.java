package li.naska.bgg.mapper;

import com.boardgamegeek.common.DecimalValue;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public class DecimalValueToBigDecimalMapper {

  public BigDecimal asString(DecimalValue value) {
    return Optional.ofNullable(value).map(DecimalValue::getValue).orElse(null);
  }

  public DecimalValue asStringValue(BigDecimal value) {
    return Optional.ofNullable(value).map(s -> {
      DecimalValue result = new DecimalValue();
      result.setValue(value);
      return result;
    }).orElse(null);
  }

}
