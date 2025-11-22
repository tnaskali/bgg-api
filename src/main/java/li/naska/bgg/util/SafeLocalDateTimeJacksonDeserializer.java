package li.naska.bgg.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;

@Slf4j
public class SafeLocalDateTimeJacksonDeserializer extends LocalDateTimeDeserializer {

  private static final DateTimeFormatter CUSTOM_FORMATTER = new DateTimeFormatterBuilder()
      .appendOptional(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
      .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
      .toFormatter();

  public static final SafeLocalDateTimeJacksonDeserializer INSTANCE =
      new SafeLocalDateTimeJacksonDeserializer();

  protected SafeLocalDateTimeJacksonDeserializer() {
    this(CUSTOM_FORMATTER);
  }

  protected SafeLocalDateTimeJacksonDeserializer(DateTimeFormatter dtf) {
    super(dtf);
  }

  protected SafeLocalDateTimeJacksonDeserializer(
      SafeLocalDateTimeJacksonDeserializer base, Boolean leniency) {
    super(base, leniency);
  }

  @Override
  public LocalDateTimeDeserializer getDelegatee() {
    return new LocalDateTimeDeserializer(_formatter);
  }

  @Override
  public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) {
    try {
      return getDelegatee().deserialize(p, ctxt);
    } catch (JacksonException dtpe) {
      // handle values formatted as 0000-00-00 00:00:00 or similar
      log.info("bad date format was mapped to null : {}", p.getString());
      return null;
    }
  }

  @Override
  protected LocalDateTimeDeserializer withDateFormat(DateTimeFormatter dtf) {
    return new SafeLocalDateTimeJacksonDeserializer(dtf);
  }

  @Override
  protected LocalDateTimeDeserializer withLeniency(Boolean leniency) {
    return new SafeLocalDateTimeJacksonDeserializer(this, leniency);
  }

  @Override
  protected LocalDateTimeDeserializer withShape(JsonFormat.Shape shape) {
    return this;
  }
}
