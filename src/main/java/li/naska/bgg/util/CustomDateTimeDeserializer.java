package li.naska.bgg.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public CustomDateTimeDeserializer() {
    super(LocalDateTime.class);
  }

  @Override
  public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    try {
      return LocalDateTime.from(FORMATTER.parse(p.getText()));
    } catch (DateTimeParseException dtpe) {
      return null;
    }
  }

}
