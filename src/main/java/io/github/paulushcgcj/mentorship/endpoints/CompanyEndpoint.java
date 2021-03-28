package io.github.paulushcgcj.mentorship.endpoints;

import io.github.paulushcgcj.mentorship.exceptions.EntryNotFoundException;
import io.github.paulushcgcj.mentorship.models.company.Company;
import io.github.paulushcgcj.mentorship.services.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@AllArgsConstructor
@Profile("annotation")
public class CompanyEndpoint {

  private final CompanyService service;

  @GetMapping
  public Mono<List<Company>> listCompanies(
    @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
    @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
    @RequestParam(value = "sort", required = false) String[] sort
  ) {
    return service.listCompanies(page, size, sort);
  }

  @GetMapping("/{companyId}")
  public Mono<Company> getCompany(@PathVariable String companyId) throws EntryNotFoundException {
    return service.getCompany(companyId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Void> addCompany(@RequestBody Company company, ServerHttpResponse response) {
    return
      service
        .addCompany(company)
        .map(companyCreated -> {
          response.getHeaders().add("Location", companyCreated.getId());
          return companyCreated;
        })
        .flatMap(companyCreated -> Mono.empty());
  }

  @PutMapping("/{companyId}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<Void> updateCompany(
    @PathVariable String companyId,
    @RequestBody Company company,
    ServerHttpResponse response
  ) {
    return
      service.updateCompany(company)
        .map(companyCreated -> {
          response.getHeaders().add("Location", companyCreated.getId());
          return companyCreated;
        })
        .flatMap(companyCreated -> Mono.empty());
  }

  @DeleteMapping("/{companyId}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<Void> removeCompany(@PathVariable String companyId) throws EntryNotFoundException {
    return service.removeCompany(companyId);
  }

}
