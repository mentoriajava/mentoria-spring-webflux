package io.github.paulushcgcj.mentorship.filters;

import io.github.paulushcgcj.mentorship.security.BodyAuthenticationConverter;
import io.github.paulushcgcj.mentorship.security.StatusCodeAuthSuccessHandler;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

public class LoginWebFilter extends AuthenticationWebFilter {

  public LoginWebFilter(
    ReactiveAuthenticationManager authenticationManager,
    ServerCodecConfigurer serverCodecConfigurer
  ) {
    super(authenticationManager);

    setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/api/login"));
    setServerAuthenticationConverter(new BodyAuthenticationConverter(serverCodecConfigurer));
    setAuthenticationSuccessHandler(new StatusCodeAuthSuccessHandler());
  }
}
