package li.naska.bgg.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeToZonedDateTimeAdapter extends XmlAdapter<String, ZonedDateTime> {

  /**
   * Example : 2004-07-31T12:59:18-05:00
   */
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

  @Override
  public String marshal(ZonedDateTime zonedDateTime) {
    return zonedDateTime.format(FORMATTER);
  }

  @Override
  public ZonedDateTime unmarshal(String string) {
    return ZonedDateTime.from(FORMATTER.parse(string));
  }
}
