package li.naska.bgg.resource;

import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractMockServerIT {

  protected static MockWebServer mockWebServer;
  @LocalServerPort
  protected int port;

  @BeforeAll
  static void setupMockWebServer() throws Exception {
    mockWebServer = new MockWebServer();
    mockWebServer.start();
  }

  @AfterAll
  static void teardownMockWebServer() throws Exception {
    mockWebServer.shutdown();
  }

  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
    registry.add("bgg.web.baseurl-bgs", () -> mockWebServer.url("/").url().toString());
  }

  protected RecordedRequest record() {
    try {
      return mockWebServer.takeRequest(1000, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      return null;
    }
  }

}
