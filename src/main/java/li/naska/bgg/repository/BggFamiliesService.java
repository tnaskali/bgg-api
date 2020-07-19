package li.naska.bgg.repository;

import com.boardgamegeek.family.Families;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BggFamiliesService {

  @Value("${bgg.endpoints.family.read}")
  private String familyReadEndpoint;

  @Autowired
  public RestTemplate restTemplate;

  public ResponseEntity<Families> getFamily(Integer id, Map<String, String> extraParams) {
    String urlParams = String.format("?id=%d", id) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = familyReadEndpoint + urlParams;
    return restTemplate.getForEntity(url, Families.class);
  }

}
