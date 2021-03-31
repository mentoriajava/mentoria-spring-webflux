package io.github.paulushcgcj.mentorship.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.github.paulushcgcj.mentorship.models.company.Company;
import reactor.test.StepVerifier;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Integration Test | Company Service")
class CompanyServiceIntegrationTest {

  @Autowired
  private CompanyService service;

  @Test
  @DisplayName("List without sorting")
  void shouldListWithoutSorting() {

    StepVerifier
      .create(
        service
          .listCompanies(
            0, 1, null
          )
      )
      .expectNext(MentorshipTestUtils.loadList("list_single_company_unsorted.json",Company.class))
      .expectComplete()
      .verify();
  }

  @Test
  @DisplayName("List with sorting")
  void shouldListWithSorting() {

    StepVerifier
      .create(
        service
          .listCompanies(
            0, 1, "name"
          )
      )
      .expectNext(MentorshipTestUtils.loadList("list_single_company_sorted.json",Company.class))
      .expectComplete()
      .verify();
  }


}
