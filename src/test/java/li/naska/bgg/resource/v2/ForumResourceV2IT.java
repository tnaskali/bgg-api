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

@DisplayName("Forum resource V2")
public class ForumResourceV2IT extends AbstractMockServerIT {

  private WebTestClient webTestClient;

  @PostConstruct
  private void postConstruct() {
    webTestClient = WebTestClient.bindToServer()
        .baseUrl("http://localhost:" + port + "/bgg-api/api/v2/forum")
        .build();
  }

  @Nested
  @DisplayName("get forum")
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
          "<forum id=\"666\" title=\"Sessions\" numthreads=\"39\" numposts=\"211\" lastpostdate=\"Thu, 01 Jan 1970 00:00:00 +0000\" noposting=\"0\" termsofuse=\"https://boardgamegeek.com/xmlapi/termsofuse\">\n" +
          "    <threads>\n" +
          "        <thread id=\"1139778\" subject=\"Strategy layers aplenty!\" author=\"N/A\" numarticles=\"4\" postdate=\"Sun, 16 Mar 2014 07:06:30 +0000\" lastpostdate=\"Mon, 17 Mar 2014 05:23:57 +0000\" />\n" +
          "        <thread id=\"989536\" subject=\"4th Annual Indonesia Night\" author=\"Jythier\" numarticles=\"7\" postdate=\"Mon, 10 Jun 2013 13:23:05 +0000\" lastpostdate=\"Tue, 11 Jun 2013 13:52:29 +0000\" />\n" +
          "        <thread id=\"326399\" subject=\"I Am Made of Lose (My First Indonesia Session)\" author=\"doubtofbuddha\" numarticles=\"9\" postdate=\"Wed, 16 Jul 2008 14:20:00 +0000\" lastpostdate=\"Mon, 19 Dec 2011 15:17:08 +0000\" />\n" +
          "        <thread id=\"446576\" subject=\"Things that Happen\" author=\"Jythier\" numarticles=\"18\" postdate=\"Sun, 27 Sep 2009 12:17:41 +0000\" lastpostdate=\"Sun, 16 Oct 2011 22:50:55 +0000\" />\n" +
          "        <thread id=\"662745\" subject=\"Introducing my friends to Indonesia!\" author=\"Jythier\" numarticles=\"6\" postdate=\"Sat, 11 Jun 2011 13:26:14 +0000\" lastpostdate=\"Mon, 13 Jun 2011 03:27:36 +0000\" />\n" +
          "        <thread id=\"644732\" subject=\"Only shipping companies? Hope your enemies use your ships!\" author=\"renard\" numarticles=\"11\" postdate=\"Thu, 21 Apr 2011 08:22:18 +0000\" lastpostdate=\"Sun, 24 Apr 2011 09:36:09 +0000\" />\n" +
          "        <thread id=\"577525\" subject=\"An Epic Game to End a Gaming Relationship\" author=\"Deep Silver\" numarticles=\"8\" postdate=\"Mon, 25 Oct 2010 01:52:22 +0000\" lastpostdate=\"Tue, 26 Oct 2010 00:34:13 +0000\" />\n" +
          "        <thread id=\"406340\" subject=\"Drowning in Siap Faji\" author=\"doubtofbuddha\" numarticles=\"5\" postdate=\"Tue, 12 May 2009 19:02:30 +0000\" lastpostdate=\"Wed, 04 Aug 2010 09:43:50 +0000\" />\n" +
          "        <thread id=\"443886\" subject=\"What Happened?\" author=\"doubtofbuddha\" numarticles=\"5\" postdate=\"Fri, 18 Sep 2009 20:20:38 +0000\" lastpostdate=\"Mon, 21 Sep 2009 03:34:07 +0000\" />\n" +
          "        <thread id=\"433271\" subject=\"I Think Three Player Indonesia Is My Favorite \" author=\"doubtofbuddha\" numarticles=\"35\" postdate=\"Sun, 16 Aug 2009 09:14:35 +0000\" lastpostdate=\"Sun, 23 Aug 2009 21:14:08 +0000\" />\n" +
          "        <thread id=\"387434\" subject=\"&quot;Mergers and Aquisitions is the name of the game&quot; with Pictures and Impressions\" author=\"Iceberg1\" numarticles=\"10\" postdate=\"Fri, 06 Mar 2009 23:03:51 +0000\" lastpostdate=\"Wed, 06 May 2009 19:08:09 +0000\" />\n" +
          "        <thread id=\"404002\" subject=\"Esperanta recenzo (review in Esperanto)\" author=\"kastel\" numarticles=\"3\" postdate=\"Mon, 04 May 2009 14:59:58 +0000\" lastpostdate=\"Tue, 05 May 2009 07:29:27 +0000\" />\n" +
          "        <thread id=\"401111\" subject=\"Pictorial Play Session &quot;Too many Ship Captains, not enough Siap Faji!&quot;\" author=\"Iceberg1\" numarticles=\"12\" postdate=\"Thu, 23 Apr 2009 22:01:19 +0000\" lastpostdate=\"Wed, 29 Apr 2009 18:23:20 +0000\" />\n" +
          "        <thread id=\"397749\" subject=\"1st of Many Indonesia games\" author=\"jabesly73\" numarticles=\"10\" postdate=\"Sun, 12 Apr 2009 03:47:39 +0000\" lastpostdate=\"Fri, 17 Apr 2009 16:50:12 +0000\" />\n" +
          "        <thread id=\"386325\" subject=\"&quot;A Tipsy, Topsy Island Archipelago&quot; with Impressions and Player Comments\" author=\"Iceberg1\" numarticles=\"6\" postdate=\"Tue, 03 Mar 2009 17:01:06 +0000\" lastpostdate=\"Fri, 20 Mar 2009 02:22:34 +0000\" />\n" +
          "        <thread id=\"380907\" subject=\"In which I sit down to a fast game of Indonesia with the castaways\" author=\"MisterCranky\" numarticles=\"1\" postdate=\"Fri, 13 Feb 2009 01:09:57 +0000\" lastpostdate=\"Fri, 13 Feb 2009 01:09:57 +0000\" />\n" +
          "        <thread id=\"369657\" subject=\"Siap Faji and Rice Rice Baby!\" author=\"doubtofbuddha\" numarticles=\"4\" postdate=\"Tue, 06 Jan 2009 16:41:54 +0000\" lastpostdate=\"Mon, 12 Jan 2009 14:53:19 +0000\" />\n" +
          "        <thread id=\"364627\" subject=\"&quot;High Quality Siap Faji for all!&quot; with impressions\" author=\"Iceberg1\" numarticles=\"9\" postdate=\"Wed, 17 Dec 2008 17:06:45 +0000\" lastpostdate=\"Thu, 18 Dec 2008 23:40:41 +0000\" />\n" +
          "        <thread id=\"356108\" subject=\"From gateway to gamers in three easy sessions: part three\" author=\"buzhannon\" numarticles=\"3\" postdate=\"Sun, 16 Nov 2008 08:25:41 +0000\" lastpostdate=\"Sun, 16 Nov 2008 21:28:55 +0000\" />\n" +
          "        <thread id=\"333470\" subject=\"Shipping rice and spice in indonesia\" author=\"Henkka\" numarticles=\"2\" postdate=\"Sat, 16 Aug 2008 20:09:51 +0000\" lastpostdate=\"Sun, 17 Aug 2008 23:59:25 +0000\" />\n" +
          "        <thread id=\"323570\" subject=\"Session report - Florida recount edition.  \" author=\"talrich\" numarticles=\"1\" postdate=\"Wed, 02 Jul 2008 14:07:26 +0000\" lastpostdate=\"Wed, 02 Jul 2008 14:07:27 +0000\" />\n" +
          "        <thread id=\"321709\" subject=\"A first four-player session\" author=\"blueatheart\" numarticles=\"5\" postdate=\"Mon, 23 Jun 2008 20:23:11 +0000\" lastpostdate=\"Tue, 24 Jun 2008 16:41:52 +0000\" />\n" +
          "        <thread id=\"269280\" subject=\"Acquire, Sell, Count Money\" author=\"1copse\" numarticles=\"9\" postdate=\"Fri, 04 Jan 2008 20:45:11 +0000\" lastpostdate=\"Sun, 11 May 2008 07:04:47 +0000\" />\n" +
          "        <thread id=\"294903\" subject=\"My microwave meal enterprise\" author=\"talrich\" numarticles=\"4\" postdate=\"Wed, 20 Feb 2008 16:19:36 +0000\" lastpostdate=\"Thu, 21 Feb 2008 16:50:30 +0000\" />\n" +
          "        <thread id=\"235183\" subject=\"5 newcomers bungle about Indonesia\" author=\"1copse\" numarticles=\"3\" postdate=\"Mon, 05 Nov 2007 21:17:09 +0000\" lastpostdate=\"Thu, 15 Nov 2007 18:00:13 +0000\" />\n" +
          "        <thread id=\"240143\" subject=\"5-player game at Euroquest\" author=\"BFoy\" numarticles=\"1\" postdate=\"Wed, 14 Nov 2007 17:30:42 +0000\" lastpostdate=\"Wed, 14 Nov 2007 17:30:42 +0000\" />\n" +
          "        <thread id=\"210807\" subject=\"Proper acquisitions rule is vital to know!\" author=\"viktor_haag\" numarticles=\"2\" postdate=\"Mon, 17 Sep 2007 20:30:46 +0000\" lastpostdate=\"Wed, 19 Sep 2007 04:37:45 +0000\" />\n" +
          "        <thread id=\"192766\" subject=\"Five new entrepeneurs\" author=\"seppo21\" numarticles=\"2\" postdate=\"Fri, 10 Aug 2007 00:29:19 +0000\" lastpostdate=\"Fri, 10 Aug 2007 10:51:34 +0000\" />\n" +
          "        <thread id=\"169718\" subject=\"Acquire-expand-merge: Not with 5p\" author=\"Paul Mackie\" numarticles=\"4\" postdate=\"Thu, 07 Jun 2007 12:43:19 +0000\" lastpostdate=\"Thu, 07 Jun 2007 19:47:40 +0000\" />\n" +
          "        <thread id=\"159787\" subject=\"We&#039;ve got to install microwave ovens...\" author=\"Paul Mackie\" numarticles=\"2\" postdate=\"Tue, 03 Apr 2007 12:58:32 +0000\" lastpostdate=\"Wed, 04 Apr 2007 11:25:06 +0000\" />\n" +
          "        <thread id=\"155351\" subject=\"Diversify\" author=\"Paul Mackie\" numarticles=\"1\" postdate=\"Thu, 08 Mar 2007 12:19:48 +0000\" lastpostdate=\"Thu, 08 Mar 2007 12:19:48 +0000\" />\n" +
          "        <thread id=\"143765\" subject=\"Four ruthless entrepreneurs exploit Indonesia\" author=\"talrich\" numarticles=\"2\" postdate=\"Thu, 04 Jan 2007 16:18:59 +0000\" lastpostdate=\"Thu, 04 Jan 2007 16:51:16 +0000\" />\n" +
          "        <thread id=\"134271\" subject=\"Indonesia for three (some musings about strategy)\" author=\"viktor_haag\" numarticles=\"3\" postdate=\"Wed, 08 Nov 2006 15:02:57 +0000\" lastpostdate=\"Fri, 10 Nov 2006 21:37:10 +0000\" />\n" +
          "        <thread id=\"134270\" subject=\"Indonesia makes a good three player game\" author=\"viktor_haag\" numarticles=\"1\" postdate=\"Wed, 08 Nov 2006 15:00:11 +0000\" lastpostdate=\"Wed, 08 Nov 2006 15:00:11 +0000\" />\n" +
          "        <thread id=\"90798\" subject=\"TVB  - 2 Player - Yes, this really does work 2 player.\" author=\"RPardoe\" numarticles=\"2\" postdate=\"Sat, 17 Dec 2005 18:06:13 +0000\" lastpostdate=\"Sun, 05 Mar 2006 10:49:27 +0000\" />\n" +
          "        <thread id=\"86769\" subject=\"First Impressions - Indonesia\" author=\"Onceler\" numarticles=\"2\" postdate=\"Mon, 14 Nov 2005 18:29:48 +0000\" lastpostdate=\"Wed, 04 Jan 2006 09:15:16 +0000\" />\n" +
          "        <thread id=\"86681\" subject=\"2 games at BGG.CON\" author=\"junesen\" numarticles=\"1\" postdate=\"Mon, 14 Nov 2005 02:34:29 +0000\" lastpostdate=\"Mon, 14 Nov 2005 02:34:29 +0000\" />\n" +
          "        <thread id=\"85240\" subject=\"Indonesia at Helcon\" author=\"msaari\" numarticles=\"1\" postdate=\"Wed, 02 Nov 2005 08:49:28 +0000\" lastpostdate=\"Wed, 02 Nov 2005 08:49:28 +0000\" />\n" +
          "        <thread id=\"84214\" subject=\"Rookie game of Indonesia\" author=\"dietevil\" numarticles=\"1\" postdate=\"Mon, 24 Oct 2005 16:06:20 +0000\" lastpostdate=\"Mon, 24 Oct 2005 16:06:20 +0000\" />\n" +
          "    </threads>\n" +
          "</forum>";

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
                    add("id", "666");
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
              assertThat(recordedRequest.getPath()).isEqualTo("/xmlapi2/forum" +
                  "?id=666" +
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
              assertThat(recordedRequest.getPath()).isEqualTo("/xmlapi2/forum" +
                  "?id=666" +
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
              result.expectBody().jsonPath("id", 666);
            }

          }

        }

      }

    }

  }

}
