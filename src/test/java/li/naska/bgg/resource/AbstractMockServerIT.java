package li.naska.bgg.resource;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.*;
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
    mockWebServer.setDispatcher(new QueueDispatcher());
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

  protected void enqueueXml(int responseCode, String mockResponseBody) {
    enqueue(new MockResponse()
        .setResponseCode(responseCode)
        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML)
        .setBody(mockResponseBody));
  }

  protected void enqueueJson(int responseCode, String mockResponseBody) {
    enqueue(new MockResponse()
        .setResponseCode(responseCode)
        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        .setBody(mockResponseBody));
  }

  protected void enqueueHtml(int responseCode, String mockResponseBody) {
    enqueue(new MockResponse()
        .setResponseCode(responseCode)
        .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML)
        .setBody(mockResponseBody));
  }

  protected void enqueue(MockResponse... responses) {
    mockWebServer.setDispatcher(new QueueDispatcher());
    Arrays.stream(responses).forEach(mockWebServer::enqueue);
  }

  @SneakyThrows
  protected String readFileContent(String fileName) {
    return new String(
        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(fileName))
            .readAllBytes());
  }

  protected RecordedRequest takeRequest() {
    try {
      return mockWebServer.takeRequest(100, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      return null;
    }
  }
}
