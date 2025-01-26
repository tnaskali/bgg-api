package li.naska.bgg.repository;

import java.nio.charset.StandardCharsets;
import li.naska.bgg.exception.UnexpectedBggResponseException;
import li.naska.bgg.repository.model.*;
import li.naska.bgg.util.HtmlProcessor;
import li.naska.bgg.util.JsonProcessor;
import li.naska.bgg.util.QueryParameters;
import lombok.extern.slf4j.Slf4j;
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

  private final HtmlProcessor htmlProcessor;

  public BggGeekaccountV3Repository(
      @Value("${bgg.endpoints.v3.geekaccount}") String endpoint,
      WebClient.Builder builder,
      JsonProcessor jsonProcessor,
      HtmlProcessor htmlProcessor) {
    this.endpoint = endpoint;
    this.webClient = builder.baseUrl(endpoint).build();
    this.jsonProcessor = jsonProcessor;
    this.htmlProcessor = htmlProcessor;
  }

  public Mono<BggGeekaccountContactV3ResponseBody> getGeekaccountContact(
      String cookie, BggGeekaccountContactV3QueryParams params) {
    return getGeekaccountContactAsHtml(cookie, params)
        .map(htmlProcessor::parse)
        .map(doc -> {
          BggGeekaccountContactV3ResponseBody result = new BggGeekaccountContactV3ResponseBody();
          result.setError(
              htmlProcessor.getFirstElementTextByClass(doc, "messagebox error").orElse(null));
          result.setUsername(htmlProcessor.getElementValueById(doc, "username").orElse(null));
          result.setFirstname(
              htmlProcessor.getElementValueById(doc, "firstname").orElse(null));
          result.setLastname(htmlProcessor.getElementValueById(doc, "lastname").orElse(null));
          result.setStreetaddr1(
              htmlProcessor.getElementValueById(doc, "streetaddr1").orElse(null));
          result.setStreetaddr2(
              htmlProcessor.getElementValueById(doc, "streetaddr2").orElse(null));
          result.setCity(htmlProcessor.getElementValueById(doc, "city").orElse(null));
          result.setState(htmlProcessor.getElementValueById(doc, "state").orElse(null));
          result.setNewstate(
              htmlProcessor.getElementValueByName(doc, "newstate").orElse(null));
          result.setZipcode(htmlProcessor.getElementValueById(doc, "zipcode").orElse(null));
          result.setCountry(
              htmlProcessor.getSelectedElementValueByName(doc, "country").orElse(null));
          result.setEmail(htmlProcessor.getElementValueById(doc, "email").orElse(null));
          result.setWebsite(htmlProcessor.getElementValueById(doc, "website").orElse(null));
          result.setPhone(htmlProcessor.getElementValueById(doc, "phone").orElse(null));
          result.setXboxlive_gamertag(
              htmlProcessor.getElementValueById(doc, "xboxlive_gamertag").orElse(null));
          result.setBattlenet_account(
              htmlProcessor.getElementValueById(doc, "battlenet_account").orElse(null));
          result.setSteam_account(
              htmlProcessor.getElementValueById(doc, "steam_account").orElse(null));
          result.setWii_friendcode(
              htmlProcessor.getElementValueById(doc, "wii_friendcode").orElse(null));
          result.setPsn_id(htmlProcessor.getElementValueById(doc, "psn_id").orElse(null));
          return result;
        })
        .doOnNext(responseBody -> {
          if (responseBody.getError() != null) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, responseBody.getError());
          }
        });
  }

  public Mono<String> getGeekaccountContactAsHtml(
      String cookie, BggGeekaccountContactV3QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder ->
            uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.TEXT_HTML)
        .acceptCharset(StandardCharsets.UTF_8)
        .header(HttpHeaders.COOKIE, cookie)
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK
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

  public Mono<BggGeekaccountContactV3ResponseBody> updateGeekaccountContact(
      String cookie,
      String username,
      String password,
      BggGeekaccountContactV3RequestBody requestBody) {
    return updateGeekaccountContactAsHtml(cookie, username, password, requestBody)
        .map(htmlProcessor::parse)
        .map(doc -> {
          BggGeekaccountContactV3ResponseBody result = new BggGeekaccountContactV3ResponseBody();
          result.setError(
              htmlProcessor.getFirstElementTextByClass(doc, "messagebox error").orElse(null));
          return result;
        })
        .doOnNext(responseBody -> {
          if (responseBody.getError() != null) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, responseBody.getError());
          }
        });
  }

  public Mono<String> updateGeekaccountContactAsHtml(
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
          if (clientResponse.statusCode() != HttpStatus.OK
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

  public Mono<BggGeekaccountToplistV3ResponseBody> updateGeekaccountToplist(
      String cookie, BggGeekaccountToplistV3RequestBody requestBody) {
    return updateGeekaccountToplistAsJson(cookie, requestBody)
        .map(body -> jsonProcessor.toJavaObject(body, BggGeekaccountToplistV3ResponseBody.class));
  }

  public Mono<String> updateGeekaccountToplistAsJson(
      String cookie, BggGeekaccountToplistV3RequestBody requestBody) {
    return webClient
        .post()
        .accept(MediaType.APPLICATION_JSON)
        .acceptCharset(StandardCharsets.UTF_8)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .header(HttpHeaders.COOKIE, cookie)
        .bodyValue(QueryParameters.fromPojo(requestBody))
        .exchangeToMono(clientResponse -> {
          if (clientResponse.statusCode() != HttpStatus.OK
              || clientResponse
                  .headers()
                  .contentType()
                  .filter(MediaType.APPLICATION_JSON::equalsTypeAndSubtype)
                  .isEmpty()) {
            throw new UnexpectedBggResponseException(clientResponse);
          }
          return clientResponse.bodyToMono(String.class).defaultIfEmpty("");
        });
  }
}
