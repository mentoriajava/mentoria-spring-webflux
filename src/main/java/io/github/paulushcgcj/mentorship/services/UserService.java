package io.github.paulushcgcj.mentorship.services;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.paulushcgcj.mentorship.exceptions.EntryNotFoundException;
import io.github.paulushcgcj.mentorship.models.user.ServiceUser;
import io.github.paulushcgcj.mentorship.repositories.GenericFileRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService {

  private final GenericFileRepository<ServiceUser> repository;

  public UserService(
    GenericFileRepository<ServiceUser> repository,
    @Value("${io.github.paulushcgcj.stub.user}") Path stubFile,
    ObjectMapper mapper
  ) {
    this.repository = repository;
    this.repository.setMapper(mapper);
    this.repository.setUp(stubFile, ServiceUser.class);
  }

  public Mono<List<ServiceUser>> listCompanies(final int page, final int size, final String... sort) {
    return repository.listEntries(page, size, userSorter(sort));
  }

  public Mono<ServiceUser> getUser(String userId) {
    return repository.findById(userId);
  }

  public Mono<ServiceUser> updateUser(ServiceUser company) {
    return
      repository
        .findById(company.getId())
        .switchIfEmpty(Mono.error(new EntryNotFoundException(company.getId())))
        .flatMap(existingCompany -> repository.save(company));
  }

  public Mono<ServiceUser> addUser(ServiceUser company) {
    return repository.save(company);
  }

  public Mono<Void> removeUser(String userId) throws EntryNotFoundException {
    return repository.remove(userId);
  }

  @SneakyThrows
  private Comparator<ServiceUser> userSorter(final String... sort) {
    if (sort != null) {
      return
        Stream
          .of(sort)
          .filter(paramName -> ServiceUser.comparingFields().contains(paramName))
          .map((Function<String, BeanComparator<ServiceUser>>) BeanComparator::new)
          .map(beanComparator -> (Comparator<ServiceUser>) beanComparator)
          .reduce((c1, c2) -> c1.reversed().thenComparing(c2.reversed()))
          .orElse(Comparator.comparing(ServiceUser::getId));
    }

    return Comparator.comparing(ServiceUser::getId).reversed();
  }
}
