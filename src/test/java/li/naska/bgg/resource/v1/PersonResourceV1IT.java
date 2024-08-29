package li.naska.bgg.resource.v1;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.function.Supplier;
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

@DisplayName("Person resource V1")
public class PersonResourceV1IT extends AbstractMockServerIT {

  private WebTestClient webTestClient;

  @PostConstruct
  private void postConstruct() {
    webTestClient = WebTestClient.bindToServer()
        .baseUrl("http://localhost:" + port + "/bgg-api/api/v1/person/{id}")
        .build();
  }

  @Nested
  @DisplayName("get person")
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
    @DisplayName("given remote repository answers 200")
    class Given {

      final String mockResponseBody =
          """
          <people termsofuse="https://boardgamegeek.com/xmlapi/termsofuse">
              <person>
                  <name>Reiner Knizia</name>
                  <description>Reiner Knizia (born 1957 in Germany) is a full-time game designer who lived for many years in England. He earned a PhD in mathematics and has previously worked in the banking industry. His first published games in 1990 were Gold Digger and Desperados. Since then he has been one of the world&#039;s most prolific game designers with more than 600 games and books published in numerous languages and countries including many global licences such as The Lord of the Rings, Game of Thrones, LEGO, Mensa, Disney, Monopoly, Playmobil, The Simpsons, Asterix, Star Trek and Star Wars. Over 13 million games and books sold worldwide. He is particularly notable for his auction trilogy and his tile-laying trilogy.  His name is pronounced &amp;quot;ry-ner k-NEE-zee-ah&amp;quot;

           Awards\s

               Complete List of Awards
               2003 Origins Awards Hall of Fame inductee.
               2008 Kinderspiel des Jahres award for Whoowasit?
               2008 Spiel des Jahres award for Keltis
          </description>
              </person>
          </people>
          """;

      @BeforeEach
      public void setup() {
        dispatch(200, mockResponseBody);
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
            (MediaType mediaType) ->
                Do.this.partialTest.apply(2, new LinkedMultiValueMap<>(), mediaType);

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
              assertThat(recordedRequest.getPath()).isEqualTo("/xmlapi/person/2");
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
              assertThat(recordedRequest.getPath()).isEqualTo("/xmlapi/person/2");
            }

            @Test
            @DisplayName("should answer 200")
            public void should_2() throws Exception {
              result.expectStatus().isOk();
            }

            @Test
            @DisplayName("should render JSON")
            public void should_3() throws Exception {
              result.expectBody().jsonPath("$.persons[0].name").isEqualTo("Reiner Knizia");
            }
          }
        }
      }
    }
  }
}
