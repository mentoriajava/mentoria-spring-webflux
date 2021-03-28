package io.github.paulushcgcj.mentorship.endpoints;

import io.github.paulushcgcj.mentorship.exceptions.MentorshipBaseException;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Objects;

@Component
@Order(-2)
@Slf4j
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

  private Mono<ServerResponse> genericErrorHandler(ServerRequest request) {

    Map<String, Object> errorPropertiesMap = getErrorAttributes(request,ErrorAttributeOptions.defaults());
    Throwable generatedException = extractError(getError(request));
    if (generatedException instanceof MentorshipBaseException) {
      MentorshipBaseException exception = (MentorshipBaseException) generatedException;
      return ServerResponse
        .status(exception.getStatusCode())
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(exception.getMessage()));
    }

    return
      ServerResponse
      .status(resolveHttpStatus(errorPropertiesMap))
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(errorPropertiesMap.get("message")));

  }

  private Throwable extractError(Throwable originalError){
    log.error("Loaded original error -> {}",originalError.getMessage());
    if(originalError.getCause() != null){
      return extractError(originalError.getCause());
    }
    return originalError;
  }

  private HttpStatus resolveHttpStatus(Map<String, Object> errorPropertiesMap) {
    return HttpStatus.resolve(Integer.parseInt(String.valueOf(errorPropertiesMap.getOrDefault("status", "500"))));
  }

}
