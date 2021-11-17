package li.naska.bgg.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class ToStringParamFunctions {

  private static final DateTimeFormatter LOCALDATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private static final DateTimeFormatter LOCALDATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd+HH:mm:ss");

  public static Function<Boolean, String> BGG_BOOLEAN_FUNCTION = b -> b ? "1" : "0";

  public static Function<BigDecimal, String> BGG_BIGDECIMAL_FUNCTION = e -> e.setScale(5, RoundingMode.FLOOR).toPlainString();

  public static Function<LocalDate, String> BGG_LOCALDATE_FUNCTION = LOCALDATE_FORMATTER::format;

  public static Function<LocalDateTime, String> BGG_LOCALDATETIME_FUNCTION = LOCALDATETIME_FORMATTER::format;

}
