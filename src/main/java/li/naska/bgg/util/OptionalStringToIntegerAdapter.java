package li.naska.bgg.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.util.ObjectUtils;

public class OptionalStringToIntegerAdapter extends XmlAdapter<String, Integer> {

  @Override
  public String marshal(Integer integer) {
    return integer == null ? "" : String.valueOf(integer);
  }

  @Override
  public Integer unmarshal(String string) {
    return ObjectUtils.isEmpty(string) ? null : Integer.valueOf(string);
  }
}
