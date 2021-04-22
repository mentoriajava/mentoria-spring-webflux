package io.github.paulushcgcj.mentorship.security;

import io.github.paulushcgcj.mentorship.filters.LoginWebFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@AllArgsConstructor
public class MentorshipSecurityConfiguration {

  public KeycloakReactiveAuthenticationManager authenticationManager(String issuerUrl, String clientId, String clientSecret) {
    return new KeycloakReactiveAuthenticationManager(issuerUrl, clientId, clientSecret);
  }

  @Bean
  public SecurityWebFilterChain filterChain(
    ServerHttpSecurity httpSecurity,
    ServerCodecConfigurer serverCodecConfigurer,
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwkSetUri,
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUrl,
    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}") String clientId,
    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}") String clientSecret
  ) {
    return
      httpSecurity
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .authorizeExchange()

        .pathMatchers(HttpMethod.OPTIONS).permitAll()
        .pathMatchers("/api/login").permitAll()

        .pathMatchers("/api").permitAll()
        .pathMatchers("/api/name").permitAll()

        .pathMatchers("/api/users").hasAnyRole("WRITER")
        .pathMatchers("/api/companies").hasAnyRole("READER")

        .anyExchange().authenticated().and()

        .formLogin().disable()
        .httpBasic().disable()
        .csrf().disable()

        .addFilterAt(new LoginWebFilter(authenticationManager(issuerUrl, clientId, clientSecret), serverCodecConfigurer), SecurityWebFiltersOrder.AUTHENTICATION)

        .oauth2ResourceServer()
        .jwt()
        .jwtDecoder(new NimbusReactiveJwtDecoder(jwkSetUri))

        .and()
        .and()
        .build();
  }

}
