package li.naska.bgg.resource.v2;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import li.naska.bgg.resource.AbstractMockServerIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
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
        enqueueXml(200, mockResponseBody);
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
            verify(0, getRequestedFor(urlEqualTo("/xmlapi/company")));
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
                          urlEqualTo("/xmlapi2/company?id=133&type=boardgamepublisher&videos=1"))
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
              () -> When_2.this.partialTest.apply(MediaType.APPLICATION_JSON);

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
                          urlEqualTo("/xmlapi2/company?id=133&type=boardgamepublisher&videos=1"))
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
              result.expectBody().jsonPath("$.items[0].id").isEqualTo(133);
            }
          }
        }
      }
    }
  }
}
