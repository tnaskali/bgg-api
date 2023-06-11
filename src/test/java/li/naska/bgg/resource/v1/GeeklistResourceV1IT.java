package li.naska.bgg.resource.v1;

import jakarta.annotation.PostConstruct;
import li.naska.bgg.resource.AbstractMockServerIT;
import okhttp3.mockwebserver.RecordedRequest;
import org.assertj.core.util.TriFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Geeklist resource V1")
public class GeeklistResourceV1IT extends AbstractMockServerIT {

  private WebTestClient webTestClient;

  @PostConstruct
  private void postConstruct() {
    webTestClient = WebTestClient.bindToServer()
        .baseUrl("http://localhost:" + port + "/bgg-api/api/v1/geeklist/{id}")
        .build();
  }

  @Nested
  @DisplayName("get geeklist")
  class Do {

    private final TriFunction<Integer, MultiValueMap<String, String>, MediaType, WebTestClient.ResponseSpec> partialTest =
        (Integer id, MultiValueMap<String, String> params, MediaType mediaType) ->
            webTestClient
                .get()
                .uri(builder -> builder.queryParams(params).build(id))
                .accept(mediaType)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange();

    @Nested
    @DisplayName("given remote repository answers 200")
    class Given {

      final String mockResponseBody = "" +
          "<geeklist id=\"666\" termsofuse=\"https://boardgamegeek.com/xmlapi/termsofuse\">\n" +
          "    <postdate>Wed, 12 Mar 2003 18:08:25 +0000</postdate>\n" +
          "    <postdate_timestamp>1047492505</postdate_timestamp>\n" +
          "    <editdate>Wed, 12 Mar 2003 18:08:25 +0000</editdate>\n" +
          "    <editdate_timestamp>1047492505</editdate_timestamp>\n" +
          "    <thumbs>3</thumbs>\n" +
          "    <numitems>8</numitems>\n" +
          "    <username>dlminsac</username>\n" +
          "    <title>Bodily Functions</title>\n" +
          "    <description>Games in which bodily functions play a prominant role.  Dedicated to Greg Schloesser.</description>\n" +
          "    <item " +
          "        id=\"8414\" objecttype=\"thing\" subtype=\"boardgame\" objectid=\"2940\"" +
          "        objectname=\"A Dog&#039;s Life\" username=\"dlminsac\" postdate=\"Wed, 12 Mar 2003 18:08:25 +0000\"" +
          "        editdate=\"Wed, 12 Mar 2003 18:08:25 +0000\" thumbs=\"0\" imageid=\"8508\">\n" +
          "        <body>Stock up on that water so you can force the other dogs to sniff your pee.</body>\n" +
          "    </item>\n" +
          "</geeklist>";

      @BeforeEach
      public void setup() {
        dispatch(200, mockResponseBody);
      }

      @Nested
      @DisplayName("when invalid parameters")
      class When_1 {

        private final Supplier<WebTestClient.ResponseSpec> test = () -> Do.this.partialTest.apply(
            666,
            new LinkedMultiValueMap<>() {{
              set("comments", "2");
            }},
            MediaType.APPLICATION_XML);

        @Nested
        @DisplayName("then")
        class Then {

          private WebTestClient.ResponseSpec result;

          private RecordedRequest recordedRequest;

          @BeforeEach
          public void setup() throws Exception {
            result = test.get();
            recordedRequest = record();
          }

          @Test
          @DisplayName("should answer 400")
          public void should_1() {
            result.expectStatus().isBadRequest();
          }

          @Test
          @DisplayName("should not forward request")
          public void should_2() {
            assertThat(recordedRequest).isNull();
          }

        }

      }

      @Nested
      @DisplayName("when valid parameters")
      class When_2 {

        private final Function<MediaType, WebTestClient.ResponseSpec> partialTest = (MediaType mediaType) -> Do.this.partialTest
            .apply(
                666,
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
        class When_2_1 {

          private final Supplier<WebTestClient.ResponseSpec> test = () -> When_2.this.partialTest
              .apply(MediaType.APPLICATION_XML);

          @Nested
          @DisplayName("then")
          class Then {

            private WebTestClient.ResponseSpec result;

            private RecordedRequest recordedRequest;

            @BeforeEach
            public void setup() throws Exception {
              result = test.get();
              recordedRequest = record();
            }

            @Test
            @DisplayName("should forward request")
            public void should_1() {
              assertThat(recordedRequest).isNotNull();
              assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET.name());
              assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_XML_VALUE);
              assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT_CHARSET)).isEqualTo(StandardCharsets.UTF_8.displayName().toLowerCase());
              assertThat(recordedRequest.getPath()).isEqualTo("/xmlapi/geeklist/666" +
                  "?comments=1" +
                  "&start=100" +
                  "&count=200");
            }

            @Test
            @DisplayName("should answer 200")
            public void should_2() throws Exception {
              result.expectStatus().isOk();
            }

            @Test
            @DisplayName("should render XML")
            public void should_3() throws Exception {
              result.expectBody().xml(mockResponseBody);
            }

          }

        }

        @Nested
        @DisplayName("when accept JSON")
        class When_2_2 {

          private final Supplier<WebTestClient.ResponseSpec> test = () -> When_2.this.partialTest
              .apply(MediaType.APPLICATION_JSON);

          @Nested
          @DisplayName("then")
          class Then {

            private WebTestClient.ResponseSpec result;

            private RecordedRequest recordedRequest;

            @BeforeEach
            public void setup() throws Exception {
              result = test.get();
              recordedRequest = record();
            }

            @Test
            @DisplayName("should forward request")
            public void should_1() {
              assertThat(recordedRequest).isNotNull();
              assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET.name());
              assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT)).isEqualTo(MediaType.APPLICATION_XML_VALUE);
              assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT_CHARSET)).isEqualTo(StandardCharsets.UTF_8.displayName().toLowerCase());
              assertThat(recordedRequest.getPath()).isEqualTo("/xmlapi/geeklist/666" +
                  "?comments=1" +
                  "&start=100" +
                  "&count=200");
            }

            @Test
            @DisplayName("should answer 200")
            public void should_2() throws Exception {
              result.expectStatus().isOk();
            }

            @Test
            @DisplayName("should render JSON")
            public void should_3() throws Exception {
              result.expectBody().jsonPath("$.id").isEqualTo(666);
            }

          }

        }

      }

    }

  }

}
