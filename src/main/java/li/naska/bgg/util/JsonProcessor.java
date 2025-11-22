package li.naska.bgg.util;

import com.jayway.jsonpath.JsonPath;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

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
    } catch (JacksonException e) {
      log.error("Error parsing response body", e);
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Error parsing server response");
    }
  }

  public <T> T toJavaObject(String jsonString, TypeReference<T> typeReference) {
    try {
      return objectMapper.readValue(jsonString, typeReference);
    } catch (JacksonException e) {
      log.error("Error parsing response body", e);
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Error parsing server response");
    }
  }

  public <T> Optional<T> jsonPathValue(String jsonString, String jsonPath) {
    try {
      return Optional.of(JsonPath.read(jsonString, jsonPath));
    } catch (Exception e) {
      log.error("Error parsing response body", e);
      return Optional.empty();
    }
  }
}
