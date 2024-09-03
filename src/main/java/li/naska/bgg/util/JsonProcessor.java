package li.naska.bgg.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@Slf4j
public class JsonProcessor {

  private final ObjectMapper objectMapper;

  public JsonProcessor(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public <T> T toJavaObject(String jsonString, Class<T> clazz) {
    try {
      return objectMapper.readValue(jsonString, clazz);
    } catch (JsonProcessingException e) {
      log.error("Error parsing response body", e);
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Error parsing server response");
    }
  }
}
