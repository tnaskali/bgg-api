package li.naska.bgg.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateAdapter extends XmlAdapter<String, LocalDate> {

  /**
   * Example : 2020-01-11
   */
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  @Override
  public String marshal(LocalDate localDate) {
    return localDate.format(FORMATTER);
  }

  @Override
  public LocalDate unmarshal(String string) {
    return LocalDate.from(FORMATTER.parse(string));
  }
}
