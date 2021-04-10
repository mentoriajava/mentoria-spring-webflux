package io.github.paulushcgcj.mentorship.security;

import io.github.paulushcgcj.mentorship.filters.LoggedWebFilter;
import io.github.paulushcgcj.mentorship.filters.LoginWebFilter;
import io.github.paulushcgcj.mentorship.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@AllArgsConstructor
public class MentorshipSecurityConfiguration {

  private UserRepository userRepository;
  private JsonWebTokenService jwtService;

  @Bean
  public ReactiveUserDetailsService userDetailsService() {
    return username -> userRepository.findByUsername(username);
  }

  @Bean
  public UserDetailsRepositoryReactiveAuthenticationManager authenticationManager() {
    return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService());
  }

  @Bean
  public SecurityWebFilterChain filterChain(
    ServerHttpSecurity httpSecurity,
    ServerCodecConfigurer serverCodecConfigurer
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

        .addFilterAt(new LoginWebFilter(authenticationManager(), serverCodecConfigurer, jwtService), SecurityWebFiltersOrder.AUTHENTICATION)
        .addFilterAt(new LoggedWebFilter(authenticationManager(), jwtService), SecurityWebFiltersOrder.LAST)


        .build();
  }

}
