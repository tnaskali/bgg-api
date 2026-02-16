package li.naska.bgg.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.util.ObjectUtils;

public class OptionalStringToLocalDateAdapter extends XmlAdapter<String, LocalDate> {

  /**
   * Example : 2020-01-11
   */
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  @Override
  public String marshal(LocalDate localDate) {
    return localDate == null ? "" : localDate.format(FORMATTER);
  }

  @Override
  public LocalDate unmarshal(String string) {
    return ObjectUtils.isEmpty(string) ? null : LocalDate.from(FORMATTER.parse(string));
  }
}
