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

@DisplayName("Person resource V2")
public class PersonResourceV2IT extends AbstractMockServerIT {

  private WebTestClient webTestClient;

  @PostConstruct
  private void postConstruct() {
    webTestClient = WebTestClient.bindToServer()
        .baseUrl("http://localhost:" + port + "/bgg-api/api/v2/person")
        .build();
  }

  @Nested
  @DisplayName("get person")
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
              <item type="boardgamedesigner" id="22">
                <thumbnail>https://cf.geekdo-images.com/47kbI7I5BAmqnU2vYlRN_w__thumb/img/Rug47DYL3UbkZTXrMLh-PPxentQ=/fit-in/200x150/filters:strip_icc()/pic453802.jpg</thumbnail>
                <image>https://cf.geekdo-images.com/47kbI7I5BAmqnU2vYlRN_w__original/img/NHKlD1bIko_AulLuMVcuGdkKp8c=/0x0/filters:format(jpeg)/pic453802.jpg</image>
                <videos total="1">
                  <video id="20978" title="Steve Jackson Extended Interview from Munchkin – TableTop ep 5" category="interview" language="English" link="http://www.youtube.com/watch?v=ScJR4w7aB-E" username="kostas" userid="11555" postdate="2012-09-10T16:12:01-05:00" />
                </videos>
              </item>
            </items>
          """;

      @BeforeEach
      public void setup() {
        dispatchXml(200, mockResponseBody);
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
          public void setup() {
            result = test.get();
            recordedRequest = takeRequest();
          }

          @Test
          @DisplayName("should answer 400")
          void should_1() {
            result.expectStatus().isBadRequest();
          }

          @Test
          @DisplayName("should not forward request")
          void should_2() {
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
                    add("id", "22");
                    add("type", "boardgamedesigner");
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
            public void setup() {
              result = test.get();
              recordedRequest = takeRequest();
            }

            @Test
            @DisplayName("should forward request")
            void should_1() {
              assertThat(recordedRequest).isNotNull();
              assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET.name());
              assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT))
                  .isEqualTo(MediaType.APPLICATION_XML_VALUE);
              assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT_CHARSET))
                  .isEqualTo(StandardCharsets.UTF_8.displayName().toLowerCase());
              assertThat(recordedRequest.getPath())
                  .isEqualTo(
                      "/xmlapi2/person" + "?id=22" + "&type=boardgamedesigner" + "&videos=1");
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

            private RecordedRequest recordedRequest;

            @BeforeEach
            public void setup() {
              result = test.get();
              recordedRequest = takeRequest();
            }

            @Test
            @DisplayName("should forward request")
            void should_1() {
              assertThat(recordedRequest).isNotNull();
              assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET.name());
              assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT))
                  .isEqualTo(MediaType.APPLICATION_XML_VALUE);
              assertThat(recordedRequest.getHeader(HttpHeaders.ACCEPT_CHARSET))
                  .isEqualTo(StandardCharsets.UTF_8.displayName().toLowerCase());
              assertThat(recordedRequest.getPath())
                  .isEqualTo(
                      "/xmlapi2/person" + "?id=22" + "&type=boardgamedesigner" + "&videos=1");
            }

            @Test
            @DisplayName("should answer 200")
            void should_2() {
              result.expectStatus().isOk();
            }

            @Test
            @DisplayName("should render JSON")
            void should_3() {
              result.expectBody().jsonPath("$.items[0].id").isEqualTo(22);
            }
          }
        }
      }
    }
  }
}
