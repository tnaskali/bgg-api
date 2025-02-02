package li.naska.bgg.resource.v1;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;

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

@DisplayName("Search resource V1")
public class SearchResourceV1IT extends AbstractMockServerIT {

  private WebTestClient webTestClient;

  @PostConstruct
  private void postConstruct() {
    webTestClient = WebTestClient.bindToServer()
        .baseUrl("http://localhost:" + port + "/bgg-api/api/v1/search")
        .build();
  }

  @Nested
  @DisplayName("get search")
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

      final String mockResponseBody = readFileContent("responses/api/v1/search/200_OK.xml");

      @BeforeEach
      public void setup() {
        enqueueXml(200, mockResponseBody);
      }

      @Nested
      @DisplayName("when invalid query parameter")
      class When_1 {

        private final Supplier<WebTestClient.ResponseSpec> test = () -> Do.this.partialTest.apply(
            new LinkedMultiValueMap<>() {
              {
                add("toto", "tata");
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
      class When_2 {

        private final Function<MediaType, WebTestClient.ResponseSpec> partialTest =
            (MediaType mediaType) -> Do.this.partialTest.apply(
                new LinkedMultiValueMap<>() {
                  {
                    add("search", "corona");
                    add("exact", "1");
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
                  getRequestedFor(urlEqualTo("/xmlapi/search?search=corona&exact=1"))
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
                  getRequestedFor(urlEqualTo("/xmlapi/search?search=corona&exact=1"))
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
                  .jsonPath("$.boardgames[0].objectid")
                  .isEqualTo(1000)
                  .jsonPath("$.boardgames[0].name.value")
                  .isEqualTo("Corona")
                  .jsonPath("$.boardgames[0].yearpublished")
                  .isEqualTo(1974);
            }
          }
        }
      }
    }
  }
}
