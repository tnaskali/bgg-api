package li.naska.bgg.repository;

import jakarta.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import li.naska.bgg.exception.UnexpectedServerResponseException;
import li.naska.bgg.repository.model.*;
import li.naska.bgg.util.JsonProcessor;
import li.naska.bgg.util.QueryParameters;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class BggGeekaccountV3Repository {

  private final String endpoint;

  private final WebClient webClient;

  private final JsonProcessor jsonProcessor;

  public BggGeekaccountV3Repository(
      @Value("${bgg.endpoints.v3.geekaccount}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor) {
    this.endpoint = endpoint;
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
  }

  public Mono<BggGeekaccountContactV3ResponseBody> getGeekaccountContact(
      String cookie, BggGeekaccountContactV3QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.TEXT_HTML)
        .acceptCharset(StandardCharsets.UTF_8)
        .header(HttpHeaders.COOKIE, cookie)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK) {
            return UnexpectedServerResponseException.from(clientResponse).buildAndThrow();
          } else if (!clientResponse
              .headers()
              .contentType()
              .map(MediaType.TEXT_HTML::equalsTypeAndSubtype)
              .orElse(false)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format(
                    "Unexpected response type: %s",
                    clientResponse.headers().contentType().orElse(null)));
          }
          return clientResponse
              .bodyToMono(String.class)
              .defaultIfEmpty("")
              .map(Jsoup::parse)
              .map(doc -> {
                BggGeekaccountContactV3ResponseBody result =
                    new BggGeekaccountContactV3ResponseBody();
                result.setError(getFirstElementValueByClass(doc, "messagebox error"));
                result.setUsername(getElementValueById(doc, "username"));
                result.setFirstname(getElementValueById(doc, "firstname"));
                result.setLastname(getElementValueById(doc, "lastname"));
                result.setStreetaddr1(getElementValueById(doc, "streetaddr1"));
                result.setStreetaddr2(getElementValueById(doc, "streetaddr2"));
                result.setCity(getElementValueById(doc, "city"));
                result.setState(getElementValueById(doc, "state"));
                result.setNewstate(getElementValueByName(doc, "newstate"));
                result.setZipcode(getElementValueById(doc, "zipcode"));
                result.setCountry(getSelectedElementValueByName(doc, "country"));
                result.setEmail(getElementValueById(doc, "email"));
                result.setWebsite(getElementValueById(doc, "website"));
                result.setPhone(getElementValueById(doc, "phone"));
                result.setXboxlive_gamertag(getElementValueById(doc, "xboxlive_gamertag"));
                result.setBattlenet_account(getElementValueById(doc, "battlenet_account"));
                result.setSteam_account(getElementValueById(doc, "steam_account"));
                result.setWii_friendcode(getElementValueById(doc, "wii_friendcode"));
                result.setPsn_id(getElementValueById(doc, "psn_id"));
                return result;
              });
        })
        .doOnNext(responseBody -> {
          if (responseBody.getError() != null) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, responseBody.getError());
          }
        });
  }

  public static @Nullable String getFirstElementValueByClass(
      Document document, String elementClass) {
    return Optional.ofNullable(document.getElementsByClass(elementClass).first())
        .map(e -> e.attr("value"))
        .orElse(null);
  }

  public static @Nullable String getElementValueById(Document document, String elementId) {
    return Optional.ofNullable(document.getElementById(elementId))
        .map(e -> e.attr("value"))
        .orElse(null);
  }

  public static @Nullable String getElementValueByName(Document document, String elementName) {
    return Optional.ofNullable(
            document.getElementsByAttributeValue("name", elementName).first())
        .map(e -> e.attr("value"))
        .orElse(null);
  }

  public static @Nullable String getSelectedElementValueByName(
      Document document, String elementId) {
    return Optional.ofNullable(document.getElementById(elementId))
        .map(Element::children)
        .flatMap(
            elements -> elements.stream().filter(e -> e.hasAttr("SELECTED")).findFirst())
        .map(e -> e.attr("value"))
        .orElse(null);
  }

  public Mono<BggGeekaccountContactV3ResponseBody> updateGeekaccountContact(
      String cookie,
      String username,
      String password,
      BggGeekaccountContactV3RequestBody requestBody) {
    return webClient
        .post()
        .accept(MediaType.TEXT_HTML)
        .acceptCharset(StandardCharsets.UTF_8)
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .header(HttpHeaders.REFERER, "%s?action=editcontact".formatted(endpoint))
        .header(HttpHeaders.COOKIE, cookie)
        .body(BodyInserters.fromMultipartData(QueryParameters.fromPojo(requestBody))
            .with("username", username)
            .with("password", password)
            .with("B1", "submit"))
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK) {
            return UnexpectedServerResponseException.from(clientResponse).buildAndThrow();
          } else if (!clientResponse
              .headers()
              .contentType()
              .map(MediaType.TEXT_HTML::equalsTypeAndSubtype)
              .orElse(false)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format(
                    "Unexpected response type: %s",
                    clientResponse.headers().contentType().orElse(null)));
          }
          return clientResponse
              .bodyToMono(String.class)
              .defaultIfEmpty("")
              .map(Jsoup::parse)
              .map(doc -> {
                BggGeekaccountContactV3ResponseBody result =
                    new BggGeekaccountContactV3ResponseBody();
                result.setError(getFirstElementValueByClass(doc, "messagebox error"));
                return result;
              });
        })
        .doOnNext(responseBody -> {
          if (responseBody.getError() != null) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, responseBody.getError());
          }
        });
  }

  public Mono<BggGeekaccountToplistV3ResponseBody> updateGeekaccountToplist(
      String cookie, BggGeekaccountToplistV3RequestBody requestBody) {
    return webClient
        .post()
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .header(HttpHeaders.COOKIE, cookie)
        .bodyValue(QueryParameters.fromPojo(requestBody))
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK) {
            return UnexpectedServerResponseException.from(clientResponse).buildAndThrow();
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("").map(body -> {
            if (clientResponse
                .headers()
                .contentType()
                .map(MediaType.TEXT_HTML::equalsTypeAndSubtype)
                .orElse(false)) {
              BggGeekaccountToplistV3ResponseBody responseBody =
                  new BggGeekaccountToplistV3ResponseBody();
              Matcher matcher = Pattern.compile("<div class='messagebox error'>([\\s\\S]*?)</div>")
                  .matcher(body);
              if (matcher.find()) {
                String error = matcher.group(1).trim();
                if ("Invalid action".equals(error)) {
                  responseBody.setError(error);
                  return responseBody;
                }
              }
              log.error("Unable to extract error from HTML body");
              responseBody.setError("Unknown error");
              return responseBody;
            } else {
              return jsonProcessor.toJavaObject(body, BggGeekaccountToplistV3ResponseBody.class);
            }
          });
        })
        .doOnNext(responseBody -> {
          if (responseBody.getError() != null) {
            if ("Invalid item".equals(responseBody.getError())) {
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid item");
            } else {
              throw new ResponseStatusException(
                  HttpStatus.INTERNAL_SERVER_ERROR, responseBody.getError());
            }
          }
        });
  }
}
