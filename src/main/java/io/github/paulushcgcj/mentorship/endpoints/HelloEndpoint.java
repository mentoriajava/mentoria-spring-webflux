package io.github.paulushcgcj.mentorship.endpoints;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class HelloEndpoint {

  @GetMapping
  public Mono<String> hello(Mono<Principal> principal) {
    return
      principal
        .map(principalData -> "Hello " + principalData.getName());
  }

  @GetMapping("/name")
  public Mono<String> hello2(@RequestParam String name) {
    return
      Mono.justOrEmpty(name).map(principalData -> "Hello " + name);
  }

}
