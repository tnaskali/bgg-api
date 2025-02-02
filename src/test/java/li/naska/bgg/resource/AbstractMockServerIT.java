package li.naska.bgg.resource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import java.util.Arrays;
import java.util.Objects;
import lombok.SneakyThrows;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock({@ConfigureWireMock(name = "bgg-service", baseUrlProperties = "bgg.web.baseurl-bgs")
})
public abstract class AbstractMockServerIT {

  @InjectWireMock("bgg-service")
  protected WireMockServer wireMock;

  @LocalServerPort
  protected int port;

  protected void enqueueXml(int responseCode, String mockResponseBody) {
    enqueue(aResponse()
        .withStatus(responseCode)
        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
        .withBody(mockResponseBody));
  }

  protected void enqueueJson(int responseCode, String mockResponseBody) {
    enqueue(aResponse()
        .withStatus(responseCode)
        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .withBody(mockResponseBody));
  }

  protected void enqueueHtml(int responseCode, String mockResponseBody) {
    enqueue(aResponse()
        .withStatus(responseCode)
        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)
        .withBody(mockResponseBody));
  }

  protected void enqueue(ResponseDefinitionBuilder... responses) {
    Arrays.stream(responses)
        .forEach(response -> wireMock.stubFor(get(anyUrl()).willReturn(response)));
  }

  @SneakyThrows
  protected String readFileContent(String fileName) {
    return new String(
        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(fileName))
            .readAllBytes());
  }

  protected void verify(int count, RequestPatternBuilder requestPatternBuilder) {
    wireMock.verify(count, requestPatternBuilder);
  }
}
