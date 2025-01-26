package li.naska.bgg.exception;

import java.util.Map;
import java.util.Optional;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
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
    if (error instanceof UnexpectedBggResponseException exception) {
      errorPropertiesMap.put("bggRequestUri", exception.getBggRequestUri());
      errorPropertiesMap.put(
          "bggResponseStatusCode", exception.getBggResponseStatusCode().value());
      errorPropertiesMap.put(
          "bggResponseContentType",
          Optional.ofNullable(exception.getBggResponseContentType())
              .map(Object::toString)
              .orElse(null));
    }
    int errorStatus =
        Optional.ofNullable((Integer) errorPropertiesMap.get("status")).orElse(500);
    return ServerResponse.status(errorStatus)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(errorPropertiesMap));
  }
}
