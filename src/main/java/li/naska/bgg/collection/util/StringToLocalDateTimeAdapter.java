package li.naska.bgg.collection.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

  /**
   * Example : 2020-01-11 11:15:53
   */
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public String marshal(LocalDateTime localDateTime) {
    return localDateTime.format(FORMATTER);
  }

  @Override
  public LocalDateTime unmarshal(String string) {
    return LocalDateTime.from(FORMATTER.parse(string));
  }

}
