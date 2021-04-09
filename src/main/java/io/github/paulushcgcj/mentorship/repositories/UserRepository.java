package io.github.paulushcgcj.mentorship.repositories;

import io.github.paulushcgcj.mentorship.models.user.ServiceUser;
import io.github.paulushcgcj.mentorship.models.user.UserGroup;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@Repository
@AllArgsConstructor
public class UserRepository extends GenericFileRepository<ServiceUser> {

  private UserPrincipalRepository repository;
  private Environment environment;

  public Mono<UserDetails> findByUsername(String username) {
    if(Arrays.asList(environment.getActiveProfiles()).contains("filesystem"))
      return findBy(usernameFilter(username),fakePassword()).map(ServiceUser::getLogin);
    return Mono.justOrEmpty(repository.findByUsername(username));
  }

  private Predicate<ServiceUser> usernameFilter(final String username) {
    return user -> user.getLogin().getUsername().equals(username);
  }

  private UnaryOperator<ServiceUser> fakePassword(){
    return user ->
      user
        .withLogin(
          user
            .getLogin()
            .withEnabled(true)
            .withGroups(Set.of(new UserGroup(1,"ROLE_READER"),new UserGroup(2,"READER")))
            .withPassword("{noop}" + user.getLogin().getPassword())
        );
  }

}
