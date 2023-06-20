package li.naska.bgg.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class SafeLocalDateJacksonDeserializer extends JSR310DateTimeDeserializerBase<LocalDate> {

  public static final SafeLocalDateJacksonDeserializer INSTANCE = new SafeLocalDateJacksonDeserializer();
  private static final DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

  protected SafeLocalDateJacksonDeserializer() {
    this(CUSTOM_FORMATTER);
  }

  protected SafeLocalDateJacksonDeserializer(DateTimeFormatter dtf) {
    super(LocalDate.class, dtf);
  }

  protected SafeLocalDateJacksonDeserializer(SafeLocalDateJacksonDeserializer base, Boolean leniency) {
    super(base, leniency);
  }

  @Override
  public JsonDeserializer<LocalDate> getDelegatee() {
    return new LocalDateDeserializer(_formatter);
  }

  @Override
  public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    try {
      return getDelegatee().deserialize(p, ctxt);
    } catch (JsonMappingException dtpe) {
      // handle values formatted as 0000-00-00 or similar
      log.info("bad date format was mapped to null : {}", p.getText());
      return null;
    }
  }

  @Override
  protected JSR310DateTimeDeserializerBase<LocalDate> withDateFormat(DateTimeFormatter dtf) {
    return new SafeLocalDateJacksonDeserializer(dtf);
  }

  @Override
  protected JSR310DateTimeDeserializerBase<LocalDate> withLeniency(Boolean leniency) {
    return new SafeLocalDateJacksonDeserializer(this, leniency);
  }

  @Override
  protected JSR310DateTimeDeserializerBase<LocalDate> withShape(JsonFormat.Shape shape) {
    return this;
  }

}
