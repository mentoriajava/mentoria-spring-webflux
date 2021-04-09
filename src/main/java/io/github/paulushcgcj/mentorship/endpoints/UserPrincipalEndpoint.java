package io.github.paulushcgcj.mentorship.endpoints;

import io.github.paulushcgcj.mentorship.models.user.ServiceUser;
import io.github.paulushcgcj.mentorship.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserPrincipalEndpoint {

  private UserService repository;

  @GetMapping
  public Mono<List<ServiceUser>> listUsers(
    @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
    @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
    @RequestParam(value = "sort", required = false) String[] sort
  ) {
    return repository.listCompanies(page,size,sort);
  }

}
