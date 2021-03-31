package io.github.paulushcgcj.mentorship.security;

import io.github.paulushcgcj.mentorship.utils.StubbingUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRepository;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class MentorshipSecurityConfiguration {

  @Bean
  public MapReactiveUserDetailsService userDetailsService() {
    UserDetails user =
      User
        .withDefaultPasswordEncoder()
        .username("jhon")
        .password("superpassword")
        .roles(StubbingUtils.loadMyRoles("WRITER"))
        .build();

    return new MapReactiveUserDetailsService(user);
  }

  @Bean
  public SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity) {
    return
      httpSecurity
        .authorizeExchange()
        .pathMatchers("/api").permitAll()
        .pathMatchers("/api/name").permitAll()
        .pathMatchers("/api/companies").hasAnyRole("READER")
        .anyExchange().authenticated()
        .and().httpBasic()
        .and().csrf().disable()
        .build();
  }

}
