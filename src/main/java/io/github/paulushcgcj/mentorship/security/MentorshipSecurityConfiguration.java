package io.github.paulushcgcj.mentorship.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
        .authorities(loadMyAuthorities("WRITER"))
        .roles(loadMyRoles("WRITER"))
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
        .and()
        .build();
  }

  private String[] loadMyRoles(String roleName) {
    final List<String> existingRoles = List.of("SUPER", "MANAGER", "WRITER", "REVIEWER", "READER");
    Map<String, String[]> roleMap =
      Map.of(
        "SUPER", new String[]{"SUPER", "MANAGER", "WRITER", "REVIEWER", "READER"},
        "MANAGER", new String[]{"MANAGER", "WRITER", "REVIEWER", "READER"},
        "WRITER", new String[]{"WRITER", "READER"},
        "REVIEWER", new String[]{"REVIEWER", "READER"},
        "READER", new String[]{"READER"}
      );
    return roleMap.get(roleName);
  }

  private String[] loadMyAuthorities(String roleName) {
    return
      Stream
        .of(loadMyRoles(roleName))
        .map(role -> "ROLE_" + role)
        .toArray(String[]::new);
  }


}
