package io.github.paulushcgcj.mentorship.services;

import io.github.paulushcgcj.mentorship.exceptions.EntryNotFoundException;
import io.github.paulushcgcj.mentorship.models.company.Company;
import io.github.paulushcgcj.mentorship.repositories.GenericFileRepository;
import io.github.paulushcgcj.mentorship.utils.StubbingUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class CompanyService {

  private final GenericFileRepository<Company> repository;

  public CompanyService(
    GenericFileRepository<Company> repository,
    @Value("${io.github.paulushcgcj.stub.company}") Path stubFile
  ) {
    this.repository = repository;
    this.repository.setUp(stubFile, Company.class);
  }

  public Mono<List<Company>> listCompanies(final int page, final int size, final String... sort) {
    return repository.listEntries(page, size, companySorter(sort));
  }

  public Mono<Company> getCompany(String companyId) {
    return repository.findById(companyId);
  }

  public Mono<Company> updateCompany(Company company) {
    return
      repository
        .findById(company.getId())
        .switchIfEmpty(Mono.error(new EntryNotFoundException(company.getId())))
        .flatMap(existingCompany -> repository.save(company));
  }

  public Mono<Company> addCompany(Company company) {
    return repository.save(company);
  }

  public Mono<Void> removeCompany(String companyId) throws EntryNotFoundException {
    return repository.remove(companyId);
  }

  /* Methods used to operate on the stubs*/
  @SneakyThrows
  private Comparator<Company> companySorter(final String... sort) {
    if (sort != null) {
      return
        Stream
          .of(sort)
          .filter(paramName -> Company.comparingFields().contains(paramName))
          .map(paramName -> new BeanComparator<Company>(StubbingUtils.getMethod(paramName)))
          .map(beanComparator -> (Comparator<Company>) beanComparator)
          .reduce((c1, c2) -> c1.reversed().thenComparing(c2.reversed()))
          .orElse(Comparator.comparing(Company::getId));
    }

    return Comparator.comparing(Company::getId).reversed();
  }

}
