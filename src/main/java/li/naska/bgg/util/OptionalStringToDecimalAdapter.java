package li.naska.bgg.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigDecimal;
import org.springframework.util.ObjectUtils;

public class OptionalStringToDecimalAdapter extends XmlAdapter<String, BigDecimal> {

  @Override
  public String marshal(BigDecimal decimal) {
    return decimal == null ? "" : decimal.toPlainString();
  }

  @Override
  public BigDecimal unmarshal(String string) {
    return ObjectUtils.isEmpty(string) ? null : new BigDecimal(string);
  }
}
