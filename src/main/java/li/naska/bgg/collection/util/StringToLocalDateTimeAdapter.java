package li.naska.bgg.collection.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringToLocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

  /**
   * Example : 2020-01-11 11:15:53
   */
  private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public String marshal(LocalDateTime localDateTime) throws Exception {
    return localDateTime.format(FORMATTER);
  }

  @Override
  public LocalDateTime unmarshal(String string) throws Exception {
    return LocalDateTime.from(FORMATTER.parse(string));
  }

}
