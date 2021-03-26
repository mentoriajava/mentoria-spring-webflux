package io.github.paulushcgcj.mentorship.services;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.paulushcgcj.mentorship.repositories.CompanyRepository;
import io.github.paulushcgcj.mentorship.exceptions.CompanyNotFoundException;
import io.github.paulushcgcj.mentorship.models.Company;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@AllArgsConstructor
public class CompanyService {

  private final CompanyRepository repository;

  public Mono<List<Company>> listCompanies(final int page, final int size, final String... sort) {
    return repository.listCompanies(page, size, sort);
  }

  public Mono<Company> getCompany(String companyId) {
    return repository.findById(companyId);
  }

  public Mono<Company> updateCompany(Company company) {
    return
      repository
        .findById(company.getId())
        .switchIfEmpty(Mono.error(new CompanyNotFoundException(company.getId())))
        .flatMap(existingCompany -> repository.save(company));
  }

  public Mono<Company> addCompany(Company company) {
    return repository.save(company);
  }

  public Mono<Void> removeCompany(String companyId) throws CompanyNotFoundException {
    return repository.remove(companyId);
  }

}
