package li.naska.bgg.resource.v2;

import jakarta.annotation.PostConstruct;
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

import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Guild resource V2")
public class GuildResourceV2IT extends AbstractMockServerIT {

  private WebTestClient webTestClient;

  @PostConstruct
  private void postConstruct() {
    webTestClient = WebTestClient.bindToServer()
        .baseUrl("http://localhost:" + port + "/bgg-api/api/v2/guild")
        .build();
  }

  @Nested
  @DisplayName("get guild")
  class Do {

    private final BiFunction<MultiValueMap<String, String>, MediaType, WebTestClient.ResponseSpec> partialTest = (MultiValueMap<String, String> params, MediaType mediaType) ->
        webTestClient
            .get()
            .uri(builder -> builder.queryParams(params).build())
            .accept(mediaType)
            .acceptCharset(StandardCharsets.UTF_8)
            .exchange();

    @Nested
    @DisplayName("given remote repository answers 200")
    class Given {

      final String mockResponseBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
          "<guild id=\"666\" name=\"Shire, Middle Earth\" created=\"Mon, 16 Mar 2009 17:25:14 +0000\" termsofuse=\"https://boardgamegeek.com/xmlapi/termsofuse\">\n" +
          "    <category>region</category>\n" +
          "    <website></website>\n" +
          "    <manager>frodo</manager>\n" +
          "    <description>This guild is designed to facilitate board gaming in the Shire.</description>\n" +
          "    <location>\n" +
          "        <addr1></addr1>\n" +
          "        <addr2></addr2>\n" +
          "        <city></city>\n" +
          "        <stateorprovince>Shire</stateorprovince>\n" +
          "        <postalcode></postalcode>\n" +
          "        <country>Middle Earth</country>\n" +
          "    </location>\n" +
          "</guild>";

      @BeforeEach
      public void setup() {
        dispatch(200, mockResponseBody);
      }

      @Nested
      @DisplayName("when invalid parameters")
      class When_1 {

        private final Supplier<WebTestClient.ResponseSpec> test = () -> Do.this.partialTest.apply(
            new LinkedMultiValueMap<>(),
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
                new LinkedMultiValueMap<String, String>() {
                  {
                    add("id", "666");
                    add("members", "1");
                    add("sort", "username");
                    add("page", "1");
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
              assertThat(recordedRequest.getPath()).isEqualTo("/xmlapi2/guild" +
                  "?id=666" +
                  "&members=1" +
                  "&sort=username" +
                  "&page=1");
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
              assertThat(recordedRequest.getPath()).isEqualTo("/xmlapi2/guild" +
                  "?id=666" +
                  "&members=1" +
                  "&sort=username" +
                  "&page=1");
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
