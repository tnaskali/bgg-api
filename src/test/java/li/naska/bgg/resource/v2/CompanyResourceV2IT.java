package li.naska.bgg.resource.v2;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import li.naska.bgg.resource.AbstractMockServerIT;
import okhttp3.mockwebserver.RecordedRequest;
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

@DisplayName("Company resource V2")
public class CompanyResourceV2IT extends AbstractMockServerIT {

  private WebTestClient webTestClient;

  @PostConstruct
  private void postConstruct() {
    webTestClient = WebTestClient.bindToServer()
        .baseUrl("http://localhost:" + port + "/bgg-api/api/v2/company")
        .build();
  }

  @Nested
  @DisplayName("get company")
  class Do {

    private final BiFunction<MultiValueMap<String, String>, MediaType, WebTestClient.ResponseSpec>
        partialTest = (MultiValueMap<String, String> params, MediaType mediaType) -> webTestClient
        .get()
        .uri(builder -> builder.queryParams(params).build())
        .accept(mediaType)
        .acceptCharset(StandardCharsets.UTF_8)
        .exchange();

    @Nested
    @DisplayName("given remote repository answers 200")
    class Given {

      final String mockResponseBody =
          """
          <?xml version="1.0" encoding="utf-8"?>
            <items termsofuse="https://boardgamegeek.com/xmlapi/termsofuse">
              <item type="boardgamepublisher" id="133">
                <thumbnail>https://cf.geekdo-images.com/Apv_YYiA6cCat8zC8MDlYQ__thumb/img/cIKT4YpD0Q2OGscFpOntqXd8wmo=/fit-in/200x150/filters:strip_icc()/pic1653744.jpg</thumbnail>
                <image>https://cf.geekdo-images.com/Apv_YYiA6cCat8zC8MDlYQ__original/img/XlnIdWzI6mc3QYaXzJeZqMlA1HA=/0x0/filters:format(jpeg)/pic1653744.jpg</image>
                <name type="primary" sortindex="1" value="Hans im Glück" />
                <description>Microbadge&amp;#10;&amp;#10; - Hans im Gl&amp;uuml;ck fan&amp;#10;&amp;#10;&amp;#10;</description>
                <link type="boardgamepublisher" id="492" value="Aladdin's Dragons" inbound="true"/>
                <link type="boardgamepublisher" id="5404" value="Amun-Re" inbound="true"/>
                <videos total="2">
                  <video id="407824" title=" SPIEL 2022 - HANS IM GLÜCK - Freddy Diebold im Interview - Spiel doch mal! " category="interview" language="German" link="http://www.youtube.com/watch?v=f4wERlsXgWU" username="Spiel doch mal" userid="1133148" postdate="2022-11-05T06:02:16-05:00" />
                  <video id="357033" title="SPIEL 2021 - HANS IM GLÜCK im Interview - Redakteur Freddy Diebold- Spiel doch mal!" category="interview" language="German" link="http://www.youtube.com/watch?v=7JyuT-ZWn4I" username="Spiel doch mal" userid="1133148" postdate="2021-12-15T01:59:54-06:00" />
                </videos>
              </item>
            </items>
            """;

      @BeforeEach
      public void setup() {
        dispatch(200, mockResponseBody);
      }

      @Nested
      @DisplayName("when invalid parameters")
      class When_1 {

        private final Supplier<WebTestClient.ResponseSpec> test =
            () -> Do.this.partialTest.apply(new LinkedMultiValueMap<>(), MediaType.APPLICATION_XML);

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

        private final Function<MediaType, WebTestClient.ResponseSpec> partialTest =
            (MediaType mediaType) -> Do.this.partialTest.apply(
                new LinkedMultiValueMap<>() {
                  {
                    add("id", "133");
                    add("type", "boardgamepublisher");
                    add("videos", "1");
                    // undeclared
                    add("undeclared_param", "abc123");
                  }
                },
                mediaType);

        @Nested
        @DisplayName("when accept XML")
        class When_2_1 {

          private final Supplier<WebTestClient.ResponseSpec> test =
              () -> When_2.this.partialTest.apply(MediaType.APPLICATION_XML);

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
              assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT))
                  .isEqualTo(MediaType.APPLICATION_XML_VALUE);
              assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT_CHARSET))
                  .isEqualTo(StandardCharsets.UTF_8.displayName().toLowerCase());
              assertThat(recordedRequest.getPath())
                  .isEqualTo(
                      "/xmlapi2/company" + "?id=133" + "&type=boardgamepublisher" + "&videos=1");
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

          private final Supplier<WebTestClient.ResponseSpec> test =
              () -> When_2.this.partialTest.apply(MediaType.APPLICATION_JSON);

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
              assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT))
                  .isEqualTo(MediaType.APPLICATION_XML_VALUE);
              assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT_CHARSET))
                  .isEqualTo(StandardCharsets.UTF_8.displayName().toLowerCase());
              assertThat(recordedRequest.getPath())
                  .isEqualTo(
                      "/xmlapi2/company" + "?id=133" + "&type=boardgamepublisher" + "&videos=1");
            }

            @Test
            @DisplayName("should answer 200")
            public void should_2() throws Exception {
              result.expectStatus().isOk();
            }

            @Test
            @DisplayName("should render JSON")
            public void should_3() throws Exception {
              result.expectBody().jsonPath("$.items[0].id").isEqualTo(133);
            }
          }
        }
      }
    }
  }
}
