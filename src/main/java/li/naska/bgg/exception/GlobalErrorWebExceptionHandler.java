package li.naska.bgg.exception;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webflux.autoconfigure.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.webflux.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

  public GlobalErrorWebExceptionHandler(
      ErrorAttributes errorAttributes,
      WebProperties webProperties,
      ApplicationContext applicationContext,
      ServerCodecConfigurer codecConfigurer) {
    super(errorAttributes, webProperties.getResources(), applicationContext);
    setMessageWriters(codecConfigurer.getWriters());
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
    return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
  }

  private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
    Map<String, Object> errorPropertiesMap = getErrorAttributes(
        request, ErrorAttributeOptions.defaults().including(ErrorAttributeOptions.Include.MESSAGE));
    Throwable error = getError(request);
    if (error instanceof BggAuthorizationException exception) {
      errorPropertiesMap.put(
          "message", "Authentication to BoardGameGeek API failed. Check your application token.");
      errorPropertiesMap.put("bggRequestUri", exception.getBggRequestUri());
      errorPropertiesMap.put(
          "bggResponseStatusCode", exception.getBggResponseStatusCode().value());
      errorPropertiesMap.put(
          "bggResponseContentType", Objects.toString(exception.getBggResponseContentType(), null));
    } else if (error instanceof UnexpectedBggResponseException exception) {
      errorPropertiesMap.put("bggRequestUri", exception.getBggRequestUri());
      errorPropertiesMap.put(
          "bggResponseStatusCode", exception.getBggResponseStatusCode().value());
      errorPropertiesMap.put(
          "bggResponseContentType", Objects.toString(exception.getBggResponseContentType(), null));
    }
    int errorStatus = Optional.ofNullable((Integer) errorPropertiesMap.get("status"))
        .orElse(HttpStatus.INTERNAL_SERVER_ERROR.value());
    return ServerResponse.status(errorStatus)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(errorPropertiesMap));
  }
}
