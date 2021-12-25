package li.naska.bgg.mapper;

import com.boardgamegeek.common.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

public interface BaseMapper {

  default <T> T getFirstValue(Iterable<T> iterable) {
    if (iterable == null) {
      return null;
    }
    Iterator<T> iterator = iterable.iterator();
    return iterator.hasNext() ? iterator.next() : null;
  }

  default String getFirstStringValue(Iterable<StringValue> iterable) {
    StringValue stringValue = getFirstValue(iterable);
    return stringValue != null ? stringValue.getValue() : null;
  }

  default Integer getFirstIntegerValue(Iterable<IntegerValue> iterable) {
    IntegerValue integerValue = getFirstValue(iterable);
    return integerValue != null ? integerValue.getValue() : null;
  }

  default BigDecimal getFirstBigDecimalValue(Iterable<DecimalValue> iterable) {
    DecimalValue decimalValue = getFirstValue(iterable);
    return decimalValue != null ? decimalValue.getValue() : null;
  }

  default LocalDate getFirstLocalDateValue(Iterable<LocalDateValue> iterable) {
    LocalDateValue localDateValue = getFirstValue(iterable);
    return localDateValue != null ? localDateValue.getValue() : null;
  }

  default LocalDateTime getFirstLocalDateTimeValue(Iterable<LocalDateTimeValue> iterable) {
    LocalDateTimeValue localDateTimeValue = getFirstValue(iterable);
    return localDateTimeValue != null ? localDateTimeValue.getValue() : null;
  }

}
