package li.naska.bgg.util;

import org.apache.logging.log4j.util.Strings;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PossiblyEmptyStringToNullableIntegerAdapter extends XmlAdapter<String, Integer> {

  @Override
  public Integer unmarshal(String string) {
    return Strings.isEmpty(string) ? null : Integer.valueOf(string);
  }

  @Override
  public String marshal(Integer integer) {
    return integer == null ? Strings.EMPTY : integer.toString();
  }

}
