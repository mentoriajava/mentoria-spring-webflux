package io.github.paulushcgcj.mentorship.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.paulushcgcj.mentorship.CompanyRepository;
import io.github.paulushcgcj.mentorship.exceptions.CompanyNotFoundException;
import io.github.paulushcgcj.mentorship.models.Company;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class CompanyService {

  private final CompanyRepository repository;

  public List<Company> listCompanies(final int page, final int size, final String... sort) {
    return repository.listCompanies(page, size, sort);
  }

  public Optional<Company> getCompany(String companyId) {
    return Optional.ofNullable(repository.findById(companyId));
  }

  public void updateCompany(Company company) throws CompanyNotFoundException {
    Company updatedCompany =
    Optional
      .ofNullable(repository.findById(company.getId()))
      .map(existingCompany -> repository.save(company))
      .orElseThrow(() -> new CompanyNotFoundException(company.getId()));
    log.info("Company {} was updated",updatedCompany.getName());
  }

  public Company addCompany(Company company) {
    return repository.save(company);
  }

  public void removeCompany(String companyId) throws CompanyNotFoundException {
    repository.remove(companyId);
  }

}
