package io.github.paulushcgcj.mentorship.endpoints;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

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
