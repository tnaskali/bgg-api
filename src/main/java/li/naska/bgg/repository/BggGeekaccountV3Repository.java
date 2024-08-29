package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import li.naska.bgg.repository.model.*;
import li.naska.bgg.util.QueryParameters;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggGeekaccountV3Repository {

  @Autowired
  private ObjectMapper objectMapper;

  private final String endpoint;

  private final WebClient webClient;

  public BggGeekaccountV3Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v3.geekaccount}") String endpoint) {
    this.endpoint = endpoint;
    this.webClient = builder.baseUrl(endpoint).build();
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
        .retrieve()
        .toEntity(String.class)
        .map(entity -> {
          Document doc = Jsoup.parse(entity.getBody());
          BggGeekaccountContactV3ResponseBody result = new BggGeekaccountContactV3ResponseBody();
          result.setUsername(doc.getElementById("username").attr("value"));
          result.setFirstname(doc.getElementById("firstname").attr("value"));
          result.setLastname(doc.getElementById("lastname").attr("value"));
          result.setStreetaddr1(doc.getElementById("streetaddr1").attr("value"));
          result.setStreetaddr2(doc.getElementById("streetaddr2").attr("value"));
          result.setCity(doc.getElementById("city").attr("value"));
          result.setState(doc.getElementById("state").attr("value"));
          result.setNewstate(doc.getElementById("state")
              .parent()
              .lastElementChild()
              .lastElementChild()
              .attr("value"));
          result.setZipcode(doc.getElementById("zipcode").attr("value"));
          result.setCountry(doc.getElementById("country").attr("value"));
          result.setEmail(doc.getElementById("email").attr("value"));
          result.setWebsite(doc.getElementById("website").attr("value"));
          result.setPhone(doc.getElementById("phone").attr("value"));
          result.setXboxlive_gamertag(doc.getElementById("xboxlive_gamertag").attr("value"));
          result.setBattlenet_account(doc.getElementById("battlenet_account").attr("value"));
          result.setSteam_account(doc.getElementById("steam_account").attr("value"));
          result.setWii_friendcode(doc.getElementById("wii_friendcode").attr("value"));
          result.setPsn_id(doc.getElementById("psn_id").attr("value"));
          return result;
        });
  }

  public Mono<BggGeekaccountContactV3ResponseBody> updateGeekaccountContact(
      String cookie, BggGeekaccountContactV3RequestBody body) {
    return webClient
        .post()
        .accept(MediaType.TEXT_HTML)
        .acceptCharset(StandardCharsets.UTF_8)
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .header(HttpHeaders.REFERER, "%s?action=editcontact".formatted(endpoint))
        .header(HttpHeaders.COOKIE, cookie)
        .body(BodyInserters.fromMultipartData(QueryParameters.fromPojo(body)))
        .retrieve()
        .toEntity(String.class)
        .map(entity -> {
          BggGeekaccountContactV3ResponseBody result = new BggGeekaccountContactV3ResponseBody();
          BeanUtils.copyProperties(body, result);
          return result;
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
        .retrieve()
        .toEntity(String.class)
        .doOnNext(entity -> {
          if (MediaType.TEXT_HTML.equalsTypeAndSubtype(entity.getHeaders().getContentType())) {
            Matcher matcher = Pattern.compile("<div class='messagebox'>([\\s\\S]*?)</div>")
                .matcher(entity.getBody());
            if (matcher.find()) {
              String error = matcher.group(1).trim();
              if ("This action requires you to <a href=\"/login?redirect=1\">login</a>."
                  .equals(error)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
              }
            }
            matcher = Pattern.compile("<div class='messagebox error'>([\\s\\S]*?)</div>")
                .matcher(entity.getBody());
            if (matcher.find()) {
              String error = matcher.group(1).trim();
              if ("Invalid action".equals(error)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action");
              }
            }
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "BGG Service error");
          }
        })
        .<BggGeekaccountToplistV3ResponseBody>handle((entity, sink) -> {
          try {
            sink.next(objectMapper.readValue(
                entity.getBody(), BggGeekaccountToplistV3ResponseBody.class));
          } catch (JsonProcessingException e) {
            sink.error(
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
          }
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
