package li.naska.bgg.resource.v2;

import li.naska.bgg.resource.AbstractMockServerIT;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
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

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ForumList resource V2")
public class ForumListResourceV2IT extends AbstractMockServerIT {

  private WebTestClient webTestClient;

  @PostConstruct
  private void postConstruct() {
    webTestClient = WebTestClient.bindToServer()
        .baseUrl("http://localhost:" + port + "/bgg-api/api/v2/forumlist")
        .build();
  }

  @Nested
  @DisplayName("get forum list")
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
          "<forums type=\"thing\" id=\"666\" termsofuse=\"https://boardgamegeek.com/xmlapi/termsofuse\">\n" +
          "    <forum id=\"123541\" groupid=\"0\" title=\"Reviews\" noposting=\"0\" description=\"Post your game reviews in this forum.  &lt;A href=&quot;/thread/59278&quot;&gt;Click here for help on writing game reviews.&lt;/A&gt;\" numthreads=\"0\" numposts=\"0\" lastpostdate=\"\" />\n" +
          "    <forum id=\"50454\" groupid=\"0\" title=\"Sessions\" noposting=\"0\" description=\"Post your session reports here.\" numthreads=\"1\" numposts=\"1\" lastpostdate=\"Mon, 01 Jan 2001 06:00:00 +0000\" />\n" +
          "    <forum id=\"72914\" groupid=\"0\" title=\"General\" noposting=\"0\" description=\"Post any related article to this game here.\" numthreads=\"1\" numposts=\"2\" lastpostdate=\"Sun, 06 Jan 2008 18:24:14 +0000\" />\n" +
          "    <forum id=\"123542\" groupid=\"0\" title=\"Rules\" noposting=\"0\" description=\"Post any rules questions you have here.\" numthreads=\"0\" numposts=\"0\" lastpostdate=\"\" />\n" +
          "    <forum id=\"123543\" groupid=\"0\" title=\"Strategy\" noposting=\"0\" description=\"Post strategy and tactics articles here.\" numthreads=\"0\" numposts=\"0\" lastpostdate=\"\" />\n" +
          "    <forum id=\"123544\" groupid=\"0\" title=\"Variants\" noposting=\"0\" description=\"Post variants to the game rules here.\" numthreads=\"0\" numposts=\"0\" lastpostdate=\"\" />\n" +
          "    <forum id=\"123545\" groupid=\"0\" title=\"News\" noposting=\"0\" description=\"Post time sensitive announcements here.\" numthreads=\"0\" numposts=\"0\" lastpostdate=\"\" />\n" +
          "    <forum id=\"2480784\" groupid=\"0\" title=\"Crowdfunding\" noposting=\"0\" description=\"Post crowdfunding / preorder content here.\" numthreads=\"0\" numposts=\"0\" lastpostdate=\"\" />\n" +
          "    <forum id=\"454182\" groupid=\"0\" title=\"Play By Forum\" noposting=\"0\" description=\"Run Play By Forum (PBF) games here.\" numthreads=\"0\" numposts=\"0\" lastpostdate=\"\" />\n" +
          "    <forum id=\"1477598\" groupid=\"0\" title=\"Organized Play\" noposting=\"0\" description=\"Post here to find local gamers and to promote local events.\" numthreads=\"0\" numposts=\"0\" lastpostdate=\"\" />\n" +
          "</forums>";

      @BeforeEach
      private void setup() {
        mockWebServer.setDispatcher(new Dispatcher() {
          @Override
          public MockResponse dispatch(RecordedRequest request) {
            return new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/xml")
                .setBody(mockResponseBody);
          }
        });
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
                    add("type", "thing");
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
              assertThat(recordedRequest.getPath()).isEqualTo("/xmlapi2/forumlist" +
                  "?id=666" +
                  "&type=thing");
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
              assertThat(recordedRequest.getPath()).isEqualTo("/xmlapi2/forumlist" +
                  "?id=666" +
                  "&type=thing");
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
