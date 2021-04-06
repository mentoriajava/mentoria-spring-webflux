package io.github.paulushcgcj.mentorship.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import io.github.paulushcgcj.mentorship.models.user.ServiceUser;
import reactor.core.publisher.Mono;

@Repository
public class UserRepository extends GenericFileRepository<ServiceUser> {


  @Autowired
  private UserPrincipalRepository repository;

  public Mono<UserDetails> findByUsername(String username){
    return Mono.justOrEmpty(repository.findByUsername(username));
  }
}
