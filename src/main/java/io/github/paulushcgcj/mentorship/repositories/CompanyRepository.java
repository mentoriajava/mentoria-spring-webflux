package io.github.paulushcgcj.mentorship.repositories;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import io.github.paulushcgcj.mentorship.models.company.Company;

@Repository
@AllArgsConstructor
public class CompanyRepository extends GenericFileRepository<Company>  {
}
