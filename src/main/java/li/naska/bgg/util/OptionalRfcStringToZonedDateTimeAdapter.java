package li.naska.bgg.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.util.ObjectUtils;

public class OptionalRfcStringToZonedDateTimeAdapter extends XmlAdapter<String, ZonedDateTime> {

  /** Example : Wed, 01 Apr 2020 19:50:39 +0000 */
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z");

  @Override
  public String marshal(ZonedDateTime zonedDateTime) {
    return zonedDateTime == null ? "" : zonedDateTime.format(FORMATTER);
  }

  @Override
  public ZonedDateTime unmarshal(String string) {
    return ObjectUtils.isEmpty(string) ? null : ZonedDateTime.from(FORMATTER.parse(string));
  }
}
