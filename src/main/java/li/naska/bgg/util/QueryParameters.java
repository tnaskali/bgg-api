package li.naska.bgg.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class QueryParameters {

  public static MultiValueMap<String, String> fromPojo(Object source) {
    MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
    Map<String, Object> fieldMap = new ObjectMapper().convertValue(source, new TypeReference<Map<String, Object>>() {
    });
    fieldMap.forEach((k, v) -> {
      if (v != null) result.set(k, v.toString());
    });
    return result;
  }

}
