package io.github.paulushcgcj.mentorship.endpoints;

import io.github.paulushcgcj.mentorship.exceptions.MentorshipBaseException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

  public GlobalErrorWebExceptionHandler(
    ErrorAttributes errorAttributes,
    WebProperties.Resources resources,
    ApplicationContext applicationContext,
    ServerCodecConfigurer codecConfigurer
  ) {
    super(errorAttributes, resources, applicationContext);
    this.setMessageWriters(codecConfigurer.getWriters());
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
    return RouterFunctions.route(RequestPredicates.all(), this::genericErrorHandler);
  }

  private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

    Map<String, Object> errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
    Throwable generatedException = getError(request).getCause();

    if (generatedException instanceof MentorshipBaseException) {
      MentorshipBaseException exception = (MentorshipBaseException) generatedException;
      return ServerResponse
        .status(exception.getStatusCode())
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(exception.getMessage()));
    }
    return
      ServerResponse
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(String.valueOf(errorAttributes.get("message"))));

  }

  private Mono<ServerResponse> genericErrorHandler(ServerRequest request) {

    Map<String, Object> errorPropertiesMap = getErrorAttributes(request,
      ErrorAttributeOptions.defaults());

    return
      ServerResponse
      .status(HttpStatus.BAD_REQUEST)
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(errorPropertiesMap));

  }

}
