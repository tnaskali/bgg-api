package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.BggGeekaccountToplistV5QueryParams;
import li.naska.bgg.repository.model.BggGeekaccountToplistV5ResponseBody;
import li.naska.bgg.repository.model.BggGeekaccountToplistV5ResponseBody.TopListItem;
import li.naska.bgg.util.HtmlProcessor;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggGeekaccountV5Repository {

  private final WebClient webClient;

  private final HtmlProcessor htmlProcessor;

  public BggGeekaccountV5Repository(
      @Value("${bgg.endpoints.v5.geekaccount}") String endpoint,
      WebClient.Builder builder,
      HtmlProcessor htmlProcessor) {
    this.webClient = builder.baseUrl(endpoint).build();
    this.htmlProcessor = htmlProcessor;
  }

  public Mono<BggGeekaccountToplistV5ResponseBody> getGeekaccountToplist(
      String cookie, BggGeekaccountToplistV5QueryParams params) {
    return getGeekaccountToplistAsHtml(cookie, params).map(htmlProcessor::parse).map(doc -> {
      List<TopListItem> ids =
          Objects.requireNonNull(doc.getElementById("toplistitems")).children().stream()
              .map(element -> {
                TopListItem item = new TopListItem();
                item.setToplistitemid(Integer.parseInt(element.attr("id").substring(12)));
                Element listname = Objects.requireNonNull(
                    element.getElementsByClass("listname").first());
                listname.children().stream()
                    .findFirst()
                    .ifPresentOrElse(
                        e -> {
                          item.setName(e.text());
                          String href = e.attr("href");
                          item.setHref(href);
                          String[] parts = href.split("/");
                          item.setType(parts[1]);
                          item.setId(Integer.parseInt(parts[2]));
                        },
                        () -> item.setName(listname.text()));
                return item;
              })
              .toList();
      BggGeekaccountToplistV5ResponseBody result = new BggGeekaccountToplistV5ResponseBody();
      result.setToplistitems(ids);
      return result;
    });
  }

  public Mono<String> getGeekaccountToplistAsHtml(
      String cookie, BggGeekaccountToplistV5QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/edit/list/{type}/{domain}")
            .build(params.getListtype(), params.getDomain()))
        .accept(MediaType.TEXT_HTML)
        .acceptCharset(StandardCharsets.UTF_8)
        .header(HttpHeaders.COOKIE, cookie)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Forum not found");
          } else if (clientResponse.statusCode() != HttpStatus.OK
              || clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.TEXT_HTML::equalsTypeAndSubtype)
                  .isEmpty()) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        });
  }
}
