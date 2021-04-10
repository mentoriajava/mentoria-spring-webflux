package io.github.paulushcgcj.mentorship.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class StatusCodeAuthSuccessHandler implements ServerAuthenticationSuccessHandler {

  @NonNull
  private JsonWebTokenService jwtService;
  private HttpStatus statusCode = HttpStatus.OK;


  public StatusCodeAuthSuccessHandler statusCode(HttpStatus statusCode) {
    this.statusCode = statusCode;
    return this;
  }

  @Override
  public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
    final String token = jwtService.createJwt(authentication);

    webFilterExchange
      .getExchange()
      .getResponse()
      .setStatusCode(statusCode);

    webFilterExchange
      .getExchange()
      .getResponse()
      .getHeaders()
      .add(HttpHeaders.AUTHORIZATION,token);

    webFilterExchange
      .getExchange()
      .getResponse()
      .addCookie(ResponseCookie.from("X-Auth",token).build());

    return Mono.empty();
  }
}

