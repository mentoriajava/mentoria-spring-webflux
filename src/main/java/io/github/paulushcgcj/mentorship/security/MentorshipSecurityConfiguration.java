package io.github.paulushcgcj.mentorship.security;

import io.github.paulushcgcj.mentorship.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class MentorshipSecurityConfiguration {

  @Autowired
  private UserRepository userRepository;

  @Bean
  public ReactiveUserDetailsService userDetailsService(){
    return username -> userRepository.findByUsername(username);
  }

  @Bean
  public SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity) {
    return
      httpSecurity
        .authorizeExchange()
        .pathMatchers("/api").permitAll()
        .pathMatchers("/api/name").permitAll()
        .pathMatchers("/api/users").permitAll()
        .pathMatchers("/api/companies").hasAnyRole("READER")
        .anyExchange().authenticated()
        .and().httpBasic()
        .and().csrf().disable()
        .build();
  }

}
