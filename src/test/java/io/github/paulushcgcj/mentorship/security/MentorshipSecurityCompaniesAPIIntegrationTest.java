package io.github.paulushcgcj.mentorship.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient(timeout = "6000")
@ExtendWith({SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@DisplayName("E2E Test | Security Companies API")
class MentorshipSecurityCompaniesAPIIntegrationTest {

  @Autowired
  private WebTestClient client;

  @Test
  @DisplayName("Read Companies API With Login")
  @WithMockUser(username = "jhon", password = "superpassword", roles = {"WRITER", "READER"})
  void shouldReadCompaniesApiWithLogin() {
    client
      .get()
      .uri("/api/companies")
      .exchange()
      .expectStatus().isOk()
      .expectBody();
  }

  @Test
  @DisplayName("Do not Read Companies API Without Login")
  void shouldReadCompaniesWithoutLogin() {
    client
      .get()
      .uri("/api/companies")
      .exchange()
      .expectStatus().isUnauthorized();
  }

}
