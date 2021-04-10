package io.github.paulushcgcj.mentorship.filters;

import io.github.paulushcgcj.mentorship.security.JsonWebTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
public class LoggedWebFilter extends AuthenticationWebFilter {
  private JsonWebTokenService jsonWebTokenService;

  public LoggedWebFilter(
    ReactiveAuthenticationManager authenticationManager,
    JsonWebTokenService jwtService) {
    super(authenticationManager);
    this.jsonWebTokenService = jwtService;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    final ServerHttpRequest request = exchange.getRequest();
    String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (StringUtils.isNotBlank(auth) && jsonWebTokenService.validateToken(auth)) {
      Authentication authentication = jsonWebTokenService.getAuthenticationFromToken(auth);
      return chain.filter(exchange.mutate().principal(Mono.just(authentication)).build());
    }
    return chain.filter(exchange);
  }
}
