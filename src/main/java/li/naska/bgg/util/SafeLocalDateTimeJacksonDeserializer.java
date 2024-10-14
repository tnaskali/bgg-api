package li.naska.bgg.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SafeLocalDateTimeJacksonDeserializer
    extends JSR310DateTimeDeserializerBase<LocalDateTime> {

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
    super(LocalDateTime.class, dtf);
  }

  protected SafeLocalDateTimeJacksonDeserializer(
      SafeLocalDateTimeJacksonDeserializer base, Boolean leniency) {
    super(base, leniency);
  }

  @Override
  public JsonDeserializer<LocalDateTime> getDelegatee() {
    return new LocalDateTimeDeserializer(_formatter);
  }

  @Override
  public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    try {
      return getDelegatee().deserialize(p, ctxt);
    } catch (JsonMappingException dtpe) {
      // handle values formatted as 0000-00-00 00:00:00 or similar
      log.info("bad date format was mapped to null : {}", p.getText());
      return null;
    }
  }

  @Override
  protected JSR310DateTimeDeserializerBase<LocalDateTime> withDateFormat(DateTimeFormatter dtf) {
    return new SafeLocalDateTimeJacksonDeserializer(dtf);
  }

  @Override
  protected JSR310DateTimeDeserializerBase<LocalDateTime> withLeniency(Boolean leniency) {
    return new SafeLocalDateTimeJacksonDeserializer(this, leniency);
  }

  @Override
  protected JSR310DateTimeDeserializerBase<LocalDateTime> withShape(JsonFormat.Shape shape) {
    return this;
  }
}
