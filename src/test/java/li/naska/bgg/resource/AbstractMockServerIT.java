package li.naska.bgg.resource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Objects;
import li.naska.bgg.util.ReflectionUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableWireMock({@ConfigureWireMock(name = "bgg-service", baseUrlProperties = "bgg.web.baseurl-bgs")
})
@ImportRuntimeHints(AbstractMockServerIT.WireMockRuntimeHints.class)
public abstract class AbstractMockServerIT {

  // Captured with GraalVM's tracing agent: `mise run native:tracing-agent`.
  static class WireMockRuntimeHints implements RuntimeHintsRegistrar {

    // Looked up by name only - no member access needed.
    private static final String[] BARE_TYPES = {
      "com.github.tomakehurst.wiremock.jetty12.Jetty12HttpServerFactory",
      "com.github.tomakehurst.wiremock.matching.AnythingPattern",
      "com.github.tomakehurst.wiremock.matching.EqualToPattern",
      "org.eclipse.jetty.client.HttpClient",
      "org.eclipse.jetty.ee11.websocket.server.JettyWebSocketServerContainer",
      "org.eclipse.jetty.http.Http10FieldPreEncoder",
      "org.eclipse.jetty.http.Http11FieldPreEncoder",
      "org.eclipse.jetty.http2.hpack.HpackFieldPreEncoder",
      "org.eclipse.jetty.reactive.client.ReactiveRequest",
      "org.eclipse.jetty.websocket.server.ServerWebSocketContainer",
      "org.wiremock.spring.ConfigureWireMock",
      "org.wiremock.spring.EnableWireMock",
      "org.wiremock.spring.InjectWireMock"
    };

    // Instantiated reflectively by Jetty's Holder#newInstance and IncludeExcludeSet.
    private static final String[] INVOKED_CONSTRUCTOR_TYPES = {
      "com.github.tomakehurst.wiremock.http.HttpHeadersJsonSerializer",
      "com.github.tomakehurst.wiremock.jetty11.WritableFileOrClasspathKeyStoreSource",
      "com.github.tomakehurst.wiremock.servlet.ContentTypeSettingFilter",
      "com.github.tomakehurst.wiremock.servlet.TrailingSlashFilter",
      "com.github.tomakehurst.wiremock.servlet.WireMockHandlerDispatchingServlet",
      "java.util.HashSet",
      "org.eclipse.jetty.ee10.servlets.CrossOriginFilter",
      "org.eclipse.jetty.http.pathmap.PathSpecSet",
      "org.eclipse.jetty.util.AsciiLowerCaseSet",
      "org.wiremock.spring.internal.WireMockContextCustomizerFactory",
      "org.wiremock.spring.internal.WireMockSpringJunitExtension",
      "org.wiremock.spring.internal.WireMockTestExecutionListener"
    };

    // Methods (some private) invoked reflectively, e.g. by Jackson serialization.
    private static final String[] INVOKED_METHOD_TYPES = {
      "com.github.tomakehurst.wiremock.http.ResponseDefinition",
      "org.eclipse.jetty.util.TypeUtil",
      "org.eclipse.jetty.util.Utf8StringBuilder"
    };

    // Xalan builds its stylesheet tree by reflectively instantiating and configuring one class
    // per construct, so this set tracks the stylesheet XMLUnit's ignoreComments() applies.
    private static final String[] XALAN_TYPES = {
      "org.apache.xalan.processor.TransformerFactoryImpl",
      "org.apache.xalan.templates.ElemApplyTemplates",
      "org.apache.xalan.templates.ElemCopy",
      "org.apache.xalan.templates.ElemForEach",
      "org.apache.xalan.templates.ElemTemplate",
      "org.apache.xalan.templates.Stylesheet",
      "org.apache.xalan.templates.StylesheetRoot",
      "org.apache.xalan.transformer.TransformerImpl",
      "org.apache.xml.dtm.ref.DTMManagerDefault",
      "org.apache.xpath.functions.FuncNot"
    };

    @Override
    public void registerHints(@NotNull RuntimeHints hints, ClassLoader classLoader) {
      hints
          .resources()
          .registerPattern("keystore")
          .registerPattern("assets")
          .registerPattern("assets/swagger-ui/index.html")
          // readFileContent() loads these by a computed name, which AOT processing can't trace.
          .registerPattern("responses/**")
          // Xalan's own service-provider lookups and serializer defaults.
          .registerPattern("META-INF/services/javax.xml.transform.TransformerFactory")
          .registerPattern("META-INF/services/org.apache.xml.dtm.DTMManager")
          .registerPattern("org/apache/xml/serializer/output_*.properties");

      // Registered as bundles rather than patterns so every locale is covered - pinning the
      // one the tracing agent happened to capture would break on a CI runner with another.
      hints
          .resources()
          .registerResourceBundle("org.apache.xalan.res.XSLTErrorResources")
          .registerResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.SAXMessages");

      // IfPresent: some are optional feature probes Jetty falls back from, so may be absent.
      Arrays.stream(BARE_TYPES)
          .forEach(className -> hints.reflection().registerTypeIfPresent(classLoader, className));

      Arrays.stream(INVOKED_CONSTRUCTOR_TYPES)
          .map(ReflectionUtils::getClass)
          .forEach(clazz ->
              hints.reflection().registerType(clazz, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS));

      Arrays.stream(INVOKED_METHOD_TYPES)
          .map(ReflectionUtils::getClass)
          .forEach(clazz ->
              hints.reflection().registerType(clazz, MemberCategory.INVOKE_DECLARED_METHODS));

      Arrays.stream(XALAN_TYPES)
          .map(ReflectionUtils::getClass)
          .forEach(clazz -> hints
              .reflection()
              .registerType(
                  clazz,
                  MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                  MemberCategory.INVOKE_DECLARED_METHODS));
    }
  }

  @InjectWireMock("bgg-service")
  protected WireMockServer wireMock;

  @LocalServerPort
  protected int port;

  // Selects Xalan over the JDK's XSLTC for WebTestClient's .xml() assertion, which compiles a
  // stylesheet - something XSLTC can only do by defining classes at runtime, which
  // native-image forbids. Must be set here and not in a static initializer, which Spring's AOT
  // processing would run in the build JVM; native-image likewise resolves ServiceLoader
  // providers at build time, so Xalan's META-INF/services entry alone doesn't select it.
  @BeforeEach
  void useInterpretiveXsltEngine() {
    System.setProperty(
        "javax.xml.transform.TransformerFactory",
        "org.apache.xalan.processor.TransformerFactoryImpl");
  }

  protected void enqueueText(int responseCode, String mockResponseBody) {
    enqueue(aResponse()
        .withStatus(responseCode)
        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
        .withBody(mockResponseBody));
  }

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
