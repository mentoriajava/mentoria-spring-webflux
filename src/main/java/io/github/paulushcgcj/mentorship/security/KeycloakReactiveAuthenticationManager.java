package io.github.paulushcgcj.mentorship.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class KeycloakReactiveAuthenticationManager implements ReactiveAuthenticationManager {

  private final String issuerUrl;
  private final String clientId;
  private final String clientSecret;

  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    return retrieveAccessFromKeycloak(authentication.getName(), (String) authentication.getCredentials())
      .map(keycloakMap ->
        new UsernamePasswordAuthenticationToken(
          keycloakMap,
          keycloakMap,
          List.of(new SimpleGrantedAuthority("user"))
        )
      );
  }

  private Mono<HashMap> retrieveAccessFromKeycloak(String username, String password) {
        return
      WebClient
        .builder()
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .build()
        .post()
        .uri(issuerUrl + "/protocol/openid-connect/token")
        .body(
          BodyInserters
            .fromFormData("client_id", clientId)
            .with("username", username)
            .with("password", password)
            .with("grant_type", "password")
            .with("client_secret", clientSecret)
        )
        .retrieve()
        .bodyToMono(HashMap.class);
  }

}
