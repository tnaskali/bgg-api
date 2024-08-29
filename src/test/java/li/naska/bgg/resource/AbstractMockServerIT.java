package li.naska.bgg.resource;

import java.util.concurrent.TimeUnit;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public abstract class AbstractMockServerIT {

  protected static MockWebServer mockWebServer;

  @LocalServerPort protected int port;

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

  protected void dispatch(int responseCode, String mockResponseBody) {
    mockWebServer.setDispatcher(
        new Dispatcher() {
          @Override
          public MockResponse dispatch(RecordedRequest request) {
            return new MockResponse()
                .setResponseCode(responseCode)
                .addHeader("Content-Type", "application/xml")
                .setBody(mockResponseBody);
          }
        });
  }

  protected RecordedRequest record() {
    try {
      return mockWebServer.takeRequest(500, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      return null;
    }
  }
}
