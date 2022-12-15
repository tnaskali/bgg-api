package li.naska.bgg.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public class IntegerToIntegerAdapter extends XmlAdapter<String, Integer> {

  @Override
  public String marshal(Integer integer) {
    return integer.toString();
  }

  @Override
  public Integer unmarshal(String string) {
    return Integer.parseInt(string);
  }

}
