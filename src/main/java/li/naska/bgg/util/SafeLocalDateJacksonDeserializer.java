package li.naska.bgg.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;

@Slf4j
public class SafeLocalDateJacksonDeserializer extends LocalDateDeserializer {

  private static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  public static final SafeLocalDateJacksonDeserializer INSTANCE =
      new SafeLocalDateJacksonDeserializer();

  protected SafeLocalDateJacksonDeserializer() {
    this(CUSTOM_FORMATTER);
  }

  protected SafeLocalDateJacksonDeserializer(DateTimeFormatter dtf) {
    super(dtf);
  }

  protected SafeLocalDateJacksonDeserializer(
      SafeLocalDateJacksonDeserializer base, Boolean leniency) {
    super(base, leniency);
  }

  @Override
  public LocalDateDeserializer getDelegatee() {
    return new LocalDateDeserializer(_formatter);
  }

  @Override
  public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) {
    try {
      return getDelegatee().deserialize(p, ctxt);
    } catch (JacksonException dtpe) {
      // handle values formatted as 0000-00-00 or similar
      log.info("bad date format was mapped to null : {}", p.getString());
      return null;
    }
  }

  @Override
  protected LocalDateDeserializer withDateFormat(DateTimeFormatter dtf) {
    return new SafeLocalDateJacksonDeserializer(dtf);
  }

  @Override
  protected LocalDateDeserializer withLeniency(Boolean leniency) {
    return new SafeLocalDateJacksonDeserializer(this, leniency);
  }

  @Override
  protected LocalDateDeserializer withShape(JsonFormat.Shape shape) {
    return this;
  }
}
