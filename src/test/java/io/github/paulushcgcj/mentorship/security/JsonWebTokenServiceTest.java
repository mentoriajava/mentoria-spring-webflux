package io.github.paulushcgcj.mentorship.security;

import io.jsonwebtoken.Claims;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Unit Test | Json Web Token Service")
class JsonWebTokenServiceTest {

  private final JsonWebTokenService service = new JsonWebTokenService("notthatsecretsecret", Duration.ofMinutes(5));

  @Test
  @DisplayName("Generate a proper token")
  void shouldGenerateToke() {

    assertThat(service.createJwt(
      Map.of(
        "roles", List.of(new SimpleGrantedAuthority("READER")),
        "mail", "user@mail.ca",
        "superuser", false,
        "bookstore-api", "a50314"
      ),
      "jamesBaxter"
      )
    )
      .isNotNull()
      .isNotEmpty()
      .has(hasCondition("2 dots", token -> token.chars().filter(dot -> dot == '.').count() == 2))
      .has(hasCondition("Base64 Header Encoded with HS512", "{\"alg\":\"HS512\"}", baseDecode(0)))
      .has(containsCondition("Base64 Payload Encoded contains bookstore-api", "\"bookstore-api\":\"a50314\"", baseDecode(1)))
      .has(containsCondition("Base64 Payload Encoded contains roles", "\"roles\":[{\"authority\":\"READER\"}]", baseDecode(1)))
      .has(containsCondition("Base64 Payload Encoded contains superuser", "\"superuser\":false,", baseDecode(1)))
      .has(containsCondition("Base64 Payload Encoded contains mail", "\"mail\":\"user@mail.ca\"", baseDecode(1)))
      .has(containsCondition("Base64 Payload Encoded contains subject", "\"sub\":\"jamesBaxter\"", baseDecode(1)))
      .has(containsCondition("Base64 Payload Encoded contains expiration date as unix epoch", "\"exp\":1", baseDecode(1)))
      .has(containsCondition("Base64 Payload Encoded contains issuing date as unix epoch", "\"iat\":1", baseDecode(1)))

    ;

  }

  @Test
  @DisplayName("Token claims are legit")
  void shouldReadTokenClaims() {

    assertThat(
      service.getAllClaimsFromToken(
        service.createJwt(
          Map.of(
            "roles", List.of(new SimpleGrantedAuthority("READER")),
            "mail", "user@mail.ca",
            "superuser", false,
            "bookstore-api", "a50314"
          ),
          "jamesBaxter"
        )
      )
    )
      .isNotNull()
      .isInstanceOf(Claims.class)
      .has(hasCondition("bookstore-api", (Claims claims) -> claims.get("bookstore-api", String.class).equals("a50314")))
      .has(hasCondition("superuser", (Claims claims) -> !claims.get("superuser", Boolean.class)))
      .has(hasCondition("mail", (Claims claims) -> claims.get("mail", String.class).equals("user@mail.ca")))
      .has(hasCondition("subject", (Claims claims) -> claims.getSubject().equals("jamesBaxter")))
      .has(hasCondition("roles", (Claims claims) -> claims.get("roles").toString() != null))
      .has(hasCondition("expiration date as unix epoch", (Claims claims) -> claims.getExpiration() != null))
      .has(hasCondition("contains issuing date as unix epoch", (Claims claims) -> claims.getIssuedAt() != null));
  }

  @Test
  @DisplayName("Username is jamesBaxter")
  void shouldReadUsername() {
    assertThat(
      service.getUsernameFromToken(
        service.createJwt(
          Map.of(
            "roles", List.of(new SimpleGrantedAuthority("READER")),
            "mail", "user@mail.ca",
            "superuser", false,
            "bookstore-api", "a50314"
          ),
          "jamesBaxter"
        )
      )
    )
      .isNotNull()
      .isNotEmpty()
      .isEqualTo("jamesBaxter");
  }

  @Test
  @DisplayName("Expires in five")
  void shouldReadExpirationDate() {
    assertThat(
      service.getExpirationDateFromToken(
        service.createJwt(
          Map.of(
            "roles", List.of(new SimpleGrantedAuthority("READER")),
            "mail", "user@mail.ca",
            "superuser", false,
            "bookstore-api", "a50314"
          ),
          "jamesBaxter"
        )
      )
    )
      .isNotNull()
      .isInstanceOf(Date.class)
      .isBefore(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES).plusSeconds(10)));
  }

  @Test
  @DisplayName("Reader Role was Read from Claim")
  void shouldReadRoles() {
    assertThat(
      service.getRolesFromToken(
        service.createJwt(
          Map.of(
            "roles", List.of(new SimpleGrantedAuthority("READER")),
            "mail", "user@mail.ca",
            "superuser", false,
            "bookstore-api", "a50314"
          ),
          "jamesBaxter"
        )
      )
    )
      .isNotNull()
      .asList()
      .hasOnlyElementsOfType(GrantedAuthority.class)
      .hasSize(1)
      .has(hasCondition("READER role", (List roles) -> roles.stream().allMatch(role -> ((GrantedAuthority) role).getAuthority().equals("[{authority=READER}]"))));
  }

  @Test
  @DisplayName("Read Authentication")
  void shouldReadAuthentication() {
    assertThat(
      service.getAuthenticationFromToken(
        service.createJwt(
          Map.of(
            "roles", List.of(new SimpleGrantedAuthority("READER")),
            "mail", "user@mail.ca",
            "superuser", false,
            "bookstore-api", "a50314"
          ),
          "jamesBaxter"
        )
      )
    )
      .isNotNull()
      .isInstanceOf(Authentication.class)
      .hasFieldOrPropertyWithValue("Principal.username", "jamesBaxter")
      .hasFieldOrProperty("authorities");
  }

  private <T> Condition<T> hasCondition(String description, Predicate<T> predicate) {
    return new Condition<>(predicate, description);
  }

  private <T> Condition<T> hasCondition(String description, T expectedValue, UnaryOperator<T> extractor) {
    return hasCondition(description, token -> extractor.apply(token).equals(expectedValue));
  }

  private Condition<String> containsCondition(String description, String expectedValue, UnaryOperator<String> extractor) {
    return hasCondition(description, token -> extractor.apply(token).contains(expectedValue));
  }

  private UnaryOperator<String> baseDecode(int position) {
    return token -> new String(Base64.getDecoder().decode(token.split("\\.")[position]));
  }

  private <T> UnaryOperator<T> debug() {
    return value -> {
      System.out.println(value);
      return value;
    };
  }

}