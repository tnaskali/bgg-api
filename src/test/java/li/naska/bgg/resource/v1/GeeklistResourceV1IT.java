package li.naska.bgg.resource.v1;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;
import li.naska.bgg.resource.AbstractMockServerIT;
import org.assertj.core.util.TriFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@DisplayName("Geeklist resource V1")
public class GeeklistResourceV1IT extends AbstractMockServerIT {

  private WebTestClient webTestClient;

  @PostConstruct
  private void postConstruct() {
    webTestClient = WebTestClient.bindToServer()
        .baseUrl("http://localhost:" + port + "/bgg-api/api/v1/geeklist/{id}")
        .responseTimeout(Duration.ofSeconds(20))
        .build();
  }

  @Nested
  @DisplayName("get geeklist")
  class Do {

    private final TriFunction<
            Object, MultiValueMap<String, String>, MediaType, WebTestClient.ResponseSpec>
        partialTest =
            (Object id, MultiValueMap<String, String> params, MediaType mediaType) -> webTestClient
                .get()
                .uri(builder -> builder.queryParams(params).build(id))
                .accept(mediaType)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange();

    @Nested
    @DisplayName("given remote repository answers 202 and then 200")
    class Given_1 {

      final String mockAcceptedBody = readFileContent("responses/api/v1/global/202_ACCEPTED.xml");
      final String mockResponseBody = readFileContent("responses/api/v1/geeklist/200_OK.xml");

      @BeforeEach
      public void setup() {
        enqueue(
            new ResponseDefinitionBuilder()
                .withStatus(202)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
                .withBody(mockAcceptedBody),
            new ResponseDefinitionBuilder()
                .withStatus(202)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
                .withBody(mockAcceptedBody),
            new ResponseDefinitionBuilder()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_XML_VALUE)
                .withBody(mockResponseBody));
      }

      @Nested
      @DisplayName("when valid request")
      class When {

        private final Supplier<WebTestClient.ResponseSpec> test = () ->
            Do.this.partialTest.apply(1000, new LinkedMultiValueMap<>(), MediaType.APPLICATION_XML);

        @Nested
        @DisplayName("then")
        class Then {

          private WebTestClient.ResponseSpec result;

          @BeforeEach
          public void setup() {
            result = test.get();
          }

          @Test
          @DisplayName("should wait and answer 200")
          void should() {
            result.expectStatus().isOk();
          }
        }
      }
    }

    @Nested
    @DisplayName("given remote repository answers 200")
    class Given_2 {

      final String mockResponseBody = readFileContent("responses/api/v1/geeklist/200_OK.xml");

      @BeforeEach
      public void setup() {
        enqueueXml(200, mockResponseBody);
      }

      @Nested
      @DisplayName("when invalid path parameter")
      class When_1 {

        private final Supplier<WebTestClient.ResponseSpec> test = () -> Do.this.partialTest.apply(
            "toto", new LinkedMultiValueMap<>(), MediaType.APPLICATION_XML);

        @Nested
        @DisplayName("then")
        class Then {

          private WebTestClient.ResponseSpec result;

          @BeforeEach
          public void setup() {
            result = test.get();
          }

          @Test
          @DisplayName("should answer 400")
          void should_1() {
            result.expectStatus().isBadRequest();
          }

          @Test
          @DisplayName("should not forward request")
          void should_2() {
            verify(0, getRequestedFor(anyUrl()));
          }
        }
      }

      @Nested
      @DisplayName("when invalid query parameter")
      class When_2 {

        private final Supplier<WebTestClient.ResponseSpec> test = () -> Do.this.partialTest.apply(
            1000,
            new LinkedMultiValueMap<>() {
              {
                set("comments", "2");
              }
            },
            MediaType.APPLICATION_XML);

        @Nested
        @DisplayName("then")
        class Then {

          private WebTestClient.ResponseSpec result;

          @BeforeEach
          public void setup() {
            result = test.get();
          }

          @Test
          @DisplayName("should answer 400")
          void should_1() {
            result.expectStatus().isBadRequest();
          }

          @Test
          @DisplayName("should not forward request")
          void should_2() {
            verify(0, getRequestedFor(anyUrl()));
          }
        }
      }

      @Nested
      @DisplayName("when valid parameters")
      class When_3 {

        private final Function<MediaType, WebTestClient.ResponseSpec> partialTest =
            (MediaType mediaType) -> Do.this.partialTest.apply(
                1000,
                new LinkedMultiValueMap<>() {
                  {
                    add("comments", "1");
                    add("start", "100");
                    add("count", "200");
                    // undeclared
                    add("undeclared_param", "abc123");
                  }
                },
                mediaType);

        @Nested
        @DisplayName("when accept XML")
        class When_3_1 {

          private final Supplier<WebTestClient.ResponseSpec> test =
              () -> When_3.this.partialTest.apply(MediaType.APPLICATION_XML);

          @Nested
          @DisplayName("then")
          class Then {

            private WebTestClient.ResponseSpec result;

            @BeforeEach
            public void setup() {
              result = test.get();
            }

            @Test
            @DisplayName("should forward request")
            void should_1() {
              verify(
                  1,
                  getRequestedFor(
                          urlEqualTo("/xmlapi/geeklist/1000?comments=1&count=200&start=100"))
                      .withHeader(HttpHeaders.ACCEPT, equalTo(MediaType.APPLICATION_XML_VALUE))
                      .withHeader(
                          HttpHeaders.ACCEPT_CHARSET,
                          equalTo(StandardCharsets.UTF_8.displayName().toLowerCase())));
            }

            @Test
            @DisplayName("should answer 200")
            void should_2() {
              result.expectStatus().isOk();
            }

            @Test
            @DisplayName("should render XML")
            void should_3() {
              result.expectBody().xml(mockResponseBody);
            }
          }
        }

        @Nested
        @DisplayName("when accept JSON")
        class When_2_2 {

          private final Supplier<WebTestClient.ResponseSpec> test =
              () -> When_3.this.partialTest.apply(MediaType.APPLICATION_JSON);

          @Nested
          @DisplayName("then")
          class Then {

            private WebTestClient.ResponseSpec result;

            @BeforeEach
            public void setup() {
              result = test.get();
            }

            @Test
            @DisplayName("should forward request")
            void should_1() {
              verify(
                  1,
                  getRequestedFor(
                          urlEqualTo("/xmlapi/geeklist/1000?comments=1&count=200&start=100"))
                      .withHeader(HttpHeaders.ACCEPT, equalTo(MediaType.APPLICATION_XML_VALUE))
                      .withHeader(
                          HttpHeaders.ACCEPT_CHARSET,
                          equalTo(StandardCharsets.UTF_8.displayName().toLowerCase())));
            }

            @Test
            @DisplayName("should answer 200")
            void should_2() {
              result.expectStatus().isOk();
            }

            @Test
            @DisplayName("should render JSON")
            void should_3() {
              result
                  .expectBody()
                  .jsonPath("$.id")
                  .isEqualTo(1000)
                  .jsonPath("$.postdate")
                  .isEqualTo("2003-11-28T16:22:03Z");
            }
          }
        }
      }
    }
  }
}
