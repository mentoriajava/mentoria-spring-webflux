package io.github.paulushcgcj.mentorship.security;

import io.github.paulushcgcj.mentorship.models.user.AuthenticationRequest;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.Collections;

public class BodyAuthenticationConverter implements ServerAuthenticationConverter {

  private final ResolvableType usernamePasswordType = ResolvableType.forClass(AuthenticationRequest.class);
  private ServerCodecConfigurer serverCodecConfigurer;

  public BodyAuthenticationConverter(ServerCodecConfigurer serverCodecConfigurer) {
    this.serverCodecConfigurer = serverCodecConfigurer;
  }

  @Override
  public Mono<Authentication> convert(ServerWebExchange exchange) {

    final ServerHttpRequest request = exchange.getRequest();
    MediaType contentType = request.getHeaders().getContentType();

    if (contentType != null && contentType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
      return serverCodecConfigurer
        .getReaders()
        .stream()
        .filter(reader -> reader.canRead(this.usernamePasswordType, MediaType.APPLICATION_JSON))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No JSON reader for AuthenticationRequest"))
        .readMono(this.usernamePasswordType, request, Collections.emptyMap())
        .cast(AuthenticationRequest.class)
        .map(o -> new UsernamePasswordAuthenticationToken(o.getUsername(), o.getPassword()));
    } else {
      return Mono.empty();
    }
  }
}
