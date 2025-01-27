package li.naska.bgg.resource;

import jakarta.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
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
    registry.add("bgg.web.baseurl-geekdo", () -> mockWebServer.url("/").url().toString());
  }

  protected void dispatchXml(int responseCode, String mockResponseBody) {
    dispatch(responseCode, MediaType.TEXT_XML, mockResponseBody);
  }

  protected void dispatchJson(int responseCode, String mockResponseBody) {
    dispatch(responseCode, MediaType.APPLICATION_JSON, mockResponseBody);
  }

  protected void dispatchHtml(int responseCode, String mockResponseBody) {
    dispatch(responseCode, MediaType.TEXT_HTML, mockResponseBody);
  }

  protected void dispatch(int responseCode, MediaType mediaType, String mockResponseBody) {
    mockWebServer.setDispatcher(new Dispatcher() {
      @Override
      public @NotNull MockResponse dispatch(@NotNull RecordedRequest request) {
        return new MockResponse()
            .setResponseCode(responseCode)
            .addHeader(HttpHeaders.CONTENT_TYPE, mediaType)
            .setBody(mockResponseBody);
      }
    });
  }

  protected RecordedRequest takeRequest() {
    try {
      return mockWebServer.takeRequest(500, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      return null;
    }
  }
}
