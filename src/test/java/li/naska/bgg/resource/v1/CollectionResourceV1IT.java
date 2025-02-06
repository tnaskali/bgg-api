package li.naska.bgg.resource.v1;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
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

@DisplayName("Collection resource V1")
public class CollectionResourceV1IT extends AbstractMockServerIT {

  private WebTestClient webTestClient;

  @PostConstruct
  private void postConstruct() {
    webTestClient = WebTestClient.bindToServer()
        .baseUrl("http://localhost:" + port + "/bgg-api/api/v1/collection/{username}")
        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
        .build();
  }

  @Nested
  @DisplayName("get collection")
  class Do {

    private final TriFunction<
            Object, MultiValueMap<String, String>, MediaType, WebTestClient.ResponseSpec>
        partialTest =
            (Object username, MultiValueMap<String, String> params, MediaType mediaType) ->
                webTestClient
                    .get()
                    .uri(builder -> builder.queryParams(params).build(username))
                    .accept(mediaType)
                    .acceptCharset(StandardCharsets.UTF_8)
                    .exchange();

    @Nested
    @DisplayName("when invalid query parameter")
    class When_1 {

      private final Supplier<WebTestClient.ResponseSpec> test = () -> Do.this.partialTest.apply(
          "gandalf",
          new LinkedMultiValueMap<>() {
            {
              set("comment", "2");
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
    @DisplayName("given remote repository answers 200 invalid username")
    class Given_1 {

      final String mockResponseBody =
          readFileContent("responses/api/v1/collection/200_BAD_REQUEST.xml");

      @BeforeEach
      public void setup() {
        enqueueXml(200, mockResponseBody);
      }

      @Nested
      @DisplayName("when valid request")
      class When {

        private final Supplier<WebTestClient.ResponseSpec> test = () -> Do.this.partialTest.apply(
            "toto_does_not_exist", new LinkedMultiValueMap<>(), MediaType.APPLICATION_XML);

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
          void should() {
            result.expectStatus().isBadRequest();
          }
        }
      }
    }

    @Nested
    @DisplayName("given remote repository answers 202 and then 200")
    class Given_2 {

      final String mockAcceptedBody = readFileContent("responses/api/v1/global/202_ACCEPTED.xml");
      final String mockResponseBody = readFileContent("responses/api/v1/collection/200_OK.xml");

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

        private final Supplier<WebTestClient.ResponseSpec> test = () -> Do.this.partialTest.apply(
            "gandalf", new LinkedMultiValueMap<>(), MediaType.APPLICATION_XML);

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
    class Given_3 {

      final String mockResponseBody = readFileContent("responses/api/v1/collection/200_OK.xml");

      @BeforeEach
      public void setup() {
        enqueueXml(200, mockResponseBody);
      }

      @Nested
      @DisplayName("when valid parameters")
      class When {

        private final Function<MediaType, WebTestClient.ResponseSpec> partialTest =
            (MediaType mediaType) -> Do.this.partialTest.apply(
                "gandalf",
                new LinkedMultiValueMap<>() {
                  {
                    add("version", "1");
                    add("brief", "1");
                    add("own", "1");
                    add("rated", "1");
                    add("played", "1");
                    add("comment", "1");
                    add("trade", "1");
                    add("want", "1");
                    add("wishlist", "1");
                    add("wishlistpriority", "1");
                    add("preordered", "1");
                    add("wanttoplay", "1");
                    add("wanttobuy", "1");
                    add("prevowned", "1");
                    add("hasparts", "1");
                    add("wantparts", "1");
                    add("minrating", "1");
                    add("rating", "1");
                    add("minbggrating", "1");
                    add("bggrating", "1");
                    add("minplays", "1");
                    add("maxplays", "1");
                    add("showprivate", "1");
                    // undeclared
                    add("undeclared_param", "abc123");
                  }
                },
                mediaType);

        @Nested
        @DisplayName("when accept XML")
        class When_3_1 {

          private final Supplier<WebTestClient.ResponseSpec> test =
              () -> When.this.partialTest.apply(MediaType.APPLICATION_XML);

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
                          urlEqualTo(
                              "/xmlapi/collection/gandalf?version=1&brief=1&own=1&rated=1&played=1&comment=1&trade=1&want=1&wishlist=1&wanttoplay=1&wanttobuy=1&prevowned=1&preordered=1&hasparts=1&wantparts=1&wishlistpriority=1&minrating=1&minbggrating=1&minplays=1&maxplays=1&showprivate=1"))
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
              () -> When.this.partialTest.apply(MediaType.APPLICATION_JSON);

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
                          urlEqualTo(
                              "/xmlapi/collection/gandalf?version=1&brief=1&own=1&rated=1&played=1&comment=1&trade=1&want=1&wishlist=1&wanttoplay=1&wanttobuy=1&prevowned=1&preordered=1&hasparts=1&wantparts=1&wishlistpriority=1&minrating=1&minbggrating=1&minplays=1&maxplays=1&showprivate=1"))
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
              result.expectBody().jsonPath("$.totalitems").isEqualTo(558);
            }
          }
        }
      }
    }
  }
}
