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

@DisplayName("Plays resource V2")
public class PlaysResourceV2IT extends AbstractMockServerIT {

  private WebTestClient webTestClient;

  @PostConstruct
  private void postConstruct() {
    webTestClient = WebTestClient.bindToServer()
        .baseUrl("http://localhost:" + port + "/bgg-api/api/v2/plays")
        .build();
  }

  @Nested
  @DisplayName("get plays")
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
          <plays username="gandalf" userid="666" total="23" page="1" termsofuse="https://boardgamegeek.com/xmlapi/termsofuse">
              <play id="666" date="2021-12-05" quantity="1" length="0" incomplete="0" nowinstats="0" location="Bag End">
                  <item name="War of the Ring: Second Edition" objecttype="thing" objectid="666">
                      <subtypes>
                          <subtype value="boardgame" />
                      </subtypes>
                  </item>
              </play>
          </plays>
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
            verify(0, getRequestedFor(anyUrl()));
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
                    add("username", "gandalf");
                    add("id", "666");
                    add("type", "thing");
                    add("mindate", "2021-01-01");
                    add("maxdate", "2021-01-31");
                    add("subtype", "boardgame");
                    add("page", "1");
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
                          urlEqualTo(
                              "/xmlapi2/plays?username=gandalf&id=666&type=thing&mindate=2021-01-01&maxdate=2021-01-31&subtype=boardgame&page=1"))
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
                          urlEqualTo(
                              "/xmlapi2/plays?username=gandalf&id=666&type=thing&mindate=2021-01-01&maxdate=2021-01-31&subtype=boardgame&page=1"))
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
              result.expectBody().jsonPath("$.plaies[0].id").isEqualTo(666);
            }
          }
        }
      }
    }
  }
}
