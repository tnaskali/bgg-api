package li.naska.bgg.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import li.naska.bgg.repository.model.*;
import li.naska.bgg.util.QueryParameters;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Repository
public class BggListitemsV4Repository {

  @Autowired private ObjectMapper objectMapper;

  private final WebClient webClient;

  public BggListitemsV4Repository(
      @Autowired WebClient.Builder builder,
      @Value("${bgg.endpoints.v4.listitems}") String endpoint) {
    this.webClient = builder.baseUrl(endpoint).build();
  }

  public Mono<BggListitemsV4ResponseBody> getListitems(BggListitemsV4QueryParams params) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.queryParams(QueryParameters.fromPojo(params)).build())
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(
            httpStatus -> httpStatus == HttpStatus.BAD_REQUEST,
            clientResponse ->
                Mono.error(
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown remote error")))
        .toEntity(String.class)
        .<BggListitemsV4ResponseBody>handle(
            (entity, sink) -> {
              try {
                sink.next(
                    objectMapper.readValue(entity.getBody(), BggListitemsV4ResponseBody.class));
              } catch (JsonProcessingException e) {
                sink.error(
                    new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
              }
            });
  }

  public Mono<BggListitemV4ResponseBody> getListitem(Integer id) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder.path("/{id}").build(id))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(
            httpStatus -> httpStatus == HttpStatus.BAD_REQUEST,
            clientResponse ->
                Mono.error(
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown remote error")))
        .toEntity(String.class)
        .<BggListitemV4ResponseBody>handle(
            (entity, sink) -> {
              try {
                sink.next(
                    objectMapper.readValue(entity.getBody(), BggListitemV4ResponseBody.class));
              } catch (JsonProcessingException e) {
                sink.error(
                    new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
              }
            });
  }

  public Mono<BggListitemCommentsV4ResponseBody> getListitemComments(
      Integer id, BggListitemCommentsV4QueryParams params) {
    return webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/{id}/comments")
                    .queryParams(QueryParameters.fromPojo(params))
                    .build(id))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(
            httpStatus -> httpStatus == HttpStatus.BAD_REQUEST,
            clientResponse ->
                Mono.error(
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown remote error")))
        .toEntity(String.class)
        .map(HttpEntity::getBody)
        .map(
            body ->
                StringUtils.isNumeric(body) ? String.format("{ \"totalCount\": %s }", body) : body)
        .<BggListitemCommentsV4ResponseBody>handle(
            (body, sink) -> {
              try {
                sink.next(objectMapper.readValue(body, BggListitemCommentsV4ResponseBody.class));
              } catch (JsonProcessingException e) {
                sink.error(
                    new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
              }
            });
  }

  public Mono<BggListitemReactionsV4ResponseBody> getListitemReactions(
      Integer id, BggListitemReactionsV4QueryParams params) {
    return webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/{id}/reactions")
                    .queryParams(QueryParameters.fromPojo(params))
                    .build(id))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(
            httpStatus -> httpStatus == HttpStatus.BAD_REQUEST,
            clientResponse ->
                Mono.error(
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown remote error")))
        .toEntity(String.class)
        .<BggListitemReactionsV4ResponseBody>handle(
            (entity, sink) -> {
              try {
                sink.next(
                    objectMapper.readValue(
                        entity.getBody(), BggListitemReactionsV4ResponseBody.class));
              } catch (JsonProcessingException e) {
                sink.error(
                    new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
              }
            });
  }

  public Mono<BggListitemTipsV4ResponseBody> getListitemTips(
      Integer id, BggListitemTipsV4QueryParams params) {
    return webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/{id}/tips")
                    .queryParams(QueryParameters.fromPojo(params))
                    .build(id))
        .accept(MediaType.APPLICATION_XML)
        .acceptCharset(StandardCharsets.UTF_8)
        .retrieve()
        .onStatus(
            httpStatus -> httpStatus == HttpStatus.BAD_REQUEST,
            clientResponse ->
                Mono.error(
                    new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown remote error")))
        .toEntity(String.class)
        .<BggListitemTipsV4ResponseBody>handle(
            (entity, sink) -> {
              try {
                sink.next(
                    objectMapper.readValue(entity.getBody(), BggListitemTipsV4ResponseBody.class));
              } catch (JsonProcessingException e) {
                sink.error(
                    new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
              }
            });
  }
}
