package li.naska.bgg.service;

import com.boardgamegeek.thread.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ThreadsService {

  private static final String THREADS_ENDPOINT_PATH = "/thread";
  @Autowired
  public RestTemplate restTemplate;
  @Value("${bgg.api.v2.baseurl-bgs}")
  private String baseurl;

  public ResponseEntity<Thread> getThread(Integer id, Map<String, String> extraParams) {
    String urlParams = String.format("?id=%d", id) + extraParams
        .entrySet()
        .stream()
        .map(entry -> String.format("&%s=%s", entry.getKey(), entry.getValue()))
        .collect(Collectors.joining());
    String url = baseurl + THREADS_ENDPOINT_PATH + urlParams;
    return restTemplate.getForEntity(url, Thread.class);
  }

}
