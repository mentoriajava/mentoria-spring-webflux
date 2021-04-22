package io.github.paulushcgcj.mentorship.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
public class StatusCodeAuthSuccessHandler implements ServerAuthenticationSuccessHandler {

  private HttpStatus statusCode = HttpStatus.OK;

  public StatusCodeAuthSuccessHandler statusCode(HttpStatus statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  @Override
  public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {

    webFilterExchange.getExchange().getResponse().setStatusCode(statusCode);
    Map keycloackMap = (Map) authentication.getPrincipal();

    webFilterExchange
      .getExchange()
      .getResponse()
      .getHeaders()
      .add(HttpHeaders.AUTHORIZATION, keycloackMap.get("access_token").toString());

    webFilterExchange
      .getExchange()
      .getResponse()
      .getHeaders()
      .add(HttpHeaders.EXPIRES,
        LocalDateTime
          .now()
          .plusSeconds(Long.parseLong(keycloackMap.get("expires_in").toString()))
          .format(DateTimeFormatter.ISO_DATE_TIME)
      );

    webFilterExchange
      .getExchange()
      .getResponse()
      .addCookie(ResponseCookie.from("X-Auth", keycloackMap.get("access_token").toString()).build());

    return Mono.empty();
  }
}

