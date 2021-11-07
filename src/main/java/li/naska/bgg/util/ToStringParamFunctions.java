package li.naska.bgg.util;

import com.boardgamegeek.enums.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ToStringParamFunctions {

  private static final DateTimeFormatter LOCALDATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private static final DateTimeFormatter LOCALDATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd+HH:mm:ss");

  public static Function<Boolean, String> BGG_BOOLEAN_FUNCTION = b -> b ? "1" : "0";

  public static Function<ObjectType, String> BGG_OBJECT_TYPE_FUNCTION = ObjectType::value;

  public static Function<ObjectSubtype, String> BGG_OBJECT_SUBTYPE_FUNCTION = ObjectSubtype::value;

  public static Function<DomainType, String> BGG_DOMAIN_TYPE_FUNCTION = DomainType::value;

  public static Function<SortType, String> BGG_SORT_TYPE_FUNCTION = SortType::value;

  public static Function<BigDecimal, String> BGG_BIGDECIMAL_FUNCTION = e -> e.setScale(5, RoundingMode.FLOOR).toPlainString();

  public static Function<LocalDate, String> BGG_LOCALDATE_FUNCTION = LOCALDATE_FORMATTER::format;

  public static Function<LocalDateTime, String> BGG_LOCALDATETIME_FUNCTION = LOCALDATETIME_FORMATTER::format;

  public static Function<List<Integer>, String> BGG_INTEGER_LIST_FUNCTION = e -> e.stream().map(Object::toString).collect(Collectors.joining(","));

  public static Function<List<FamilyType>, String> BGG_FAMILY_TYPE_LIST_FUNCTION = e -> e.stream().map(FamilyType::value).collect(Collectors.joining(","));

  public static Function<List<ObjectSubtype>, String> BGG_OBJECT_SUBTYPE_LIST_FUNCTION = e -> e.stream().map(ObjectSubtype::value).collect(Collectors.joining(","));

}
