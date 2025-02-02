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

@DisplayName("Collection resource V2")
public class CollectionResourceV2IT extends AbstractMockServerIT {

  private WebTestClient webTestClient;

  @PostConstruct
  private void postConstruct() {
    webTestClient = WebTestClient.bindToServer()
        .baseUrl("http://localhost:" + port + "/bgg-api/api/v2/collection")
        .build();
  }

  @Nested
  @DisplayName("get collection")
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
          <items totalitems="389" termsofuse="https://boardgamegeek.com/xmlapi/termsofuse" pubdate="Sat, 18 Dec 2021 12:16:30 +0000">
              <item objecttype="thing" objectid="94246" subtype="boardgame" collid="67682351">
                  <name sortindex="1">1812: The Invasion of Canada</name>
                  <status own="1" prevowned="0" fortrade="0" want="0" wanttoplay="0" wanttobuy="0" wishlist="0"  preordered="0" lastmodified="2020-01-11 11:15:53" />
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
            verify(0, getRequestedFor(urlEqualTo("/xmlapi/collection")));
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
                    add("version", "1");
                    add("subtype", "boardgame");
                    add("excludesubtype", "boardgameexpansion");
                    add("id", "666");
                    add("brief", "1");
                    add("stats", "1");
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
                    add("collid", "1");
                    add("modifiedsince", "2021-01-31 00:00:00");
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
                              "/xmlapi2/collection?username=gandalf&version=1&subtype=boardgame&excludesubtype=boardgameexpansion&id=666&brief=1&stats=1&own=1&rated=1&played=1&comment=1&trade=1&want=1&wishlist=1&wishlistpriority=1&preordered=1&wanttoplay=1&wanttobuy=1&prevowned=1&hasparts=1&wantparts=1&minrating=1&rating=1&minbggrating=1&bggrating=1&minplays=1&maxplays=1&showprivate=1&collid=1&modifiedsince=2021-01-31%2000:00:00"))
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
                              "/xmlapi2/collection?username=gandalf&version=1&subtype=boardgame&excludesubtype=boardgameexpansion&id=666&brief=1&stats=1&own=1&rated=1&played=1&comment=1&trade=1&want=1&wishlist=1&wishlistpriority=1&preordered=1&wanttoplay=1&wanttobuy=1&prevowned=1&hasparts=1&wantparts=1&minrating=1&rating=1&minbggrating=1&bggrating=1&minplays=1&maxplays=1&showprivate=1&collid=1&modifiedsince=2021-01-31%2000:00:00"))
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
              result.expectBody().jsonPath("$.totalitems").isEqualTo(389);
            }
          }
        }
      }
    }
  }
}
