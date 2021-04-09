package io.github.paulushcgcj.mentorship.endpoints;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@Slf4j
public class HelloEndpoint {

  @GetMapping
  public Mono<String> hello(Mono<Principal> principal) {
    return
      principal
        .map(principalData -> {
          log.info("Requesting Hello from principal as {}", principalData);
          return principalData.getName();
        })
        .switchIfEmpty(Mono.just("but you didn't told me your name!"))
        .flatMap(this::sayHello);
  }

  @GetMapping("/name")
  public Mono<String> hello2(@RequestParam(required = false) String name) {
    log.info("Requesting Hello with name as {}", name);
    return sayHello(name);
  }

  private Mono<String> sayHello(String name) {
    return Mono.justOrEmpty(name).map(principalData -> "Hello " + name);
  }

}
