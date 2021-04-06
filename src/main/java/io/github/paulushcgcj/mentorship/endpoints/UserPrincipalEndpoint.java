package io.github.paulushcgcj.mentorship.endpoints;

import io.github.paulushcgcj.mentorship.models.user.UserPrincipalInfo;
import io.github.paulushcgcj.mentorship.repositories.UserPrincipalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/users")
public class UserPrincipalEndpoint {

  @Autowired
  private UserPrincipalRepository repository;

  @GetMapping
  public Flux<UserPrincipalInfo> listUsers(){
    return Flux.fromIterable(repository.findAll());
  }

}
