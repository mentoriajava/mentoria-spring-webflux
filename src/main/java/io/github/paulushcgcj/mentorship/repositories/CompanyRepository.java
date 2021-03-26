package io.github.paulushcgcj.mentorship.repositories;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.paulushcgcj.mentorship.exceptions.CompanyNotFoundException;
import io.github.paulushcgcj.mentorship.models.Company;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class CompanyRepository {

  @Value("${io.github.paulushcgcj.stub}")
  private Path stubFile;

  private List<Company> stubbedCompanies;

  @PostConstruct
  public void setUp() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.findAndRegisterModules();

    log.info("Started to load stubbed data");

    try {
      stubbedCompanies = new ArrayList<>(List.of(
        mapper
          .readValue(stubFile.normalize().toFile(), Company[].class)
      ));
    } catch (IOException e) {
      log.error("Error while loading stub file {}", stubFile, e);
      stubbedCompanies = new ArrayList<>();
    }

    log.info("Loaded {} stubbed entries", stubbedCompanies.size());
  }

  public Mono<List<Company>> listCompanies(final int page, final int size, final String... sort) {
    return
      Mono.just(
        stubbedCompanies
          .stream()
          .sorted(companySorter(sort))
          .skip((long) page * size)
          .limit(size)
          .collect(Collectors.toList())
      );
  }

  public Mono<Company> findById(String companyId) {
    return
      Mono.justOrEmpty(
        stubbedCompanies
          .stream()
          .filter(company -> company.getId().equals(companyId))
          .findFirst()
      ).switchIfEmpty(Mono.error(new CompanyNotFoundException(companyId)));
  }

  public Mono<Company> save(Company company) {
    if (StringUtils.isNotBlank(company.getId())) {
      stubbedCompanies.removeIf(company1 -> company1.getId().equals(company.getId()));
      stubbedCompanies.add(company);
      return Mono.just(company);
    } else {
      stubbedCompanies.add(company.withId(idGen()));
      return Mono.just(company.withId(idGen()));
    }
  }

  public Mono<Void> remove(String companyId) {
    if (!stubbedCompanies.removeIf(company -> company.getId().equals(companyId)))
      return Mono.error(new CompanyNotFoundException(companyId));
    return Mono.empty();
  }

  /* Methods used to operate on the stubs*/
  @SneakyThrows
  private Comparator<Company> companySorter(final String... sort) {
    if (sort != null) {
      return
        Stream
          .of(sort)
          .filter(paramName -> Company.comparingFields().contains(paramName))
          .map(paramName -> new BeanComparator<Company>(getMethod(paramName)))
          .map(beanComparator -> (Comparator<Company>) beanComparator)
          .reduce((c1, c2) -> c1.reversed().thenComparing(c2.reversed()))
          .orElse(Comparator.comparing(Company::getId));
    }

    return Comparator.comparing(Company::getId).reversed();
  }

  private String getMethod(String value) {
    return
      "get"
        +
        value
          .substring(0, 1)
          .toUpperCase()
        +
        value
          .substring(1);
  }

  private String idGen() {
    return UUID.randomUUID().toString().replace("-", "").substring(0, 24);
  }
}
