package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import li.naska.bgg.repository.model.BggGeekaccountToplistV5QueryParams;
import li.naska.bgg.repository.model.BggGeekaccountToplistV5ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class BggGeekaccountV5Repository {

  private final WebClient webClient;

  public BggGeekaccountV5Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v5.geekaccount}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<BggGeekaccountToplistV5ResponseBody> getGeekaccountToplist(
      String cookie, BggGeekaccountToplistV5QueryParams params) {
    return webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/edit/list/{type}/{domain}")
                    .build(params.getListtype(), params.getDomain()))
        .accept(MediaType.TEXT_HTML)
        .acceptCharset(StandardCharsets.UTF_8)
        .header(HttpHeaders.COOKIE, cookie)
        .retrieve()
        .toEntity(String.class)
        .map(
            entity -> {
              Document doc = Jsoup.parse(entity.getBody());
              List<Integer> ids =
                  doc.getElementById("toplistitems").children().stream()
                      .map(element -> element.attr("id"))
                      .map(id -> id.substring(12))
                      .map(Integer::parseInt)
                      .toList();
              BggGeekaccountToplistV5ResponseBody result =
                  new BggGeekaccountToplistV5ResponseBody();
              result.setToplistitems(ids);
              return result;
            });
  }
}
