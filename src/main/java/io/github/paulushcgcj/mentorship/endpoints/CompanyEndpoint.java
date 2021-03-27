package io.github.paulushcgcj.mentorship.endpoints;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.github.paulushcgcj.mentorship.exceptions.CompanyNotFoundException;
import io.github.paulushcgcj.mentorship.models.Company;
import io.github.paulushcgcj.mentorship.services.CompanyService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/companies")
@AllArgsConstructor
public class CompanyEndpoint {

  private final CompanyService service;

  @GetMapping
  public Mono<List<Company>> listCompanies(
    @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
    @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
    @RequestParam(value = "sort", required = false) String[] sort
  ) {
    return Mono.just(service.listCompanies(page, size, sort));
  }

  @GetMapping("/{companyId}")
  public Mono<Company> getCompany(@PathVariable String companyId) throws CompanyNotFoundException {
    return service
      .getCompany(companyId)
      .map(Mono::just)
      .orElseThrow(() -> new CompanyNotFoundException(companyId));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Void> addCompany(@RequestBody Company company, ServerHttpResponse response) {
    Company createdCompany = service.addCompany(company);
    response.getHeaders().add("Location", createdCompany.getId());
    return Mono.empty();
  }

  @PutMapping("/{companyId}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<Void> updateCompany(
    @PathVariable String companyId,
    @RequestBody Company company,
    ServerHttpResponse response
  ) throws CompanyNotFoundException {
    service.updateCompany(company);
    response.getHeaders().add("Location", companyId);
    return Mono.empty();
  }

  @DeleteMapping("/{companyId}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public Mono<Void> removeCompany(@PathVariable String companyId) throws CompanyNotFoundException {
    service.removeCompany(companyId);
    return Mono.empty();
  }

}
