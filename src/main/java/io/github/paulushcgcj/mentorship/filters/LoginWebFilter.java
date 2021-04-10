package io.github.paulushcgcj.mentorship.filters;

import io.github.paulushcgcj.mentorship.security.BodyAuthenticationConverter;
import io.github.paulushcgcj.mentorship.security.JsonWebTokenService;
import io.github.paulushcgcj.mentorship.security.StatusCodeAuthSuccessHandler;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

public class LoginWebFilter extends AuthenticationWebFilter {

  private final ServerCodecConfigurer serverCodecConfigurer;

  public LoginWebFilter(
    ReactiveAuthenticationManager authenticationManager,
    ServerCodecConfigurer serverCodecConfigurer,
    JsonWebTokenService jwtService) {
    super(authenticationManager);
    this.serverCodecConfigurer = serverCodecConfigurer;

    setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/api/login"));
    setServerAuthenticationConverter(new BodyAuthenticationConverter(this.serverCodecConfigurer));
    setAuthenticationSuccessHandler(new StatusCodeAuthSuccessHandler(jwtService));
  }
}
