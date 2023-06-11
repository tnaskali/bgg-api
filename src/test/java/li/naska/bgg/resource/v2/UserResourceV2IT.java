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

@DisplayName("User resource V2")
public class UserResourceV2IT extends AbstractMockServerIT {

  private WebTestClient webTestClient;

  @PostConstruct
  private void postConstruct() {
    webTestClient = WebTestClient.bindToServer()
        .baseUrl("http://localhost:" + port + "/bgg-api/api/v2/user")
        .build();
  }

  @Nested
  @DisplayName("get user")
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
          "<user id=\"666\" name=\"gandalf\" termsofuse=\"https://boardgamegeek.com/xmlapi/termsofuse\">\n" +
          "    <firstname value=\"N/A\" />\n" +
          "    <lastname value=\"Mithrandir\" />\n" +
          "    <avatarlink value=\"N/A\" />\n" +
          "    <yearregistered value=\"2003\" />\n" +
          "    <lastlogin value=\"\" />\n" +
          "    <stateorprovince value=\"Unspecified\" />\n" +
          "    <country value=\"New Zealand\" />\n" +
          "    <webaddress value=\"\" />\n" +
          "    <xboxaccount value=\"\" />\n" +
          "    <wiiaccount value=\"\" />\n" +
          "    <psnaccount value=\"\" />\n" +
          "    <battlenetaccount value=\"\" />\n" +
          "    <steamaccount value=\"\" />\n" +
          "    <traderating value=\"0\" />\n" +
          "    <marketrating value=\"0\" />\n" +
          "</user>";

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
                new LinkedMultiValueMap<>() {
                  {
                    add("name", "gandalf");
                    add("buddies", "1");
                    add("guilds", "1");
                    add("hot", "1");
                    add("top", "1");
                    add("domain", "boardgame");
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
              assertThat(recordedRequest.getPath()).isEqualTo("/xmlapi2/user" +
                  "?name=gandalf" +
                  "&buddies=1" +
                  "&guilds=1" +
                  "&hot=1" +
                  "&top=1" +
                  "&domain=boardgame" +
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
              assertThat(recordedRequest.getPath()).isEqualTo("/xmlapi2/user" +
                  "?name=gandalf" +
                  "&buddies=1" +
                  "&guilds=1" +
                  "&hot=1" +
                  "&top=1" +
                  "&domain=boardgame" +
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
              result.expectBody()
                  .jsonPath("$.id").isEqualTo(666)
                  .jsonPath("$.name").isEqualTo("gandalf");
            }

          }

        }

      }

    }

  }

}
