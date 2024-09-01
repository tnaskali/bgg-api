package li.naska.bgg.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component
public class ServerResponseBodyMapper {

  private final ObjectMapper objectMapper;

  public ServerResponseBodyMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public <T> T parse(String responseBody, Class<T> clazz) {
    try {
      return objectMapper.readValue(responseBody, clazz);
    } catch (JsonProcessingException e) {
      log.error("Error parsing response body", e);
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Error parsing server response");
    }
  }
}
