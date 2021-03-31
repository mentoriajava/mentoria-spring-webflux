
package io.github.paulushcgcj.mentorship.models.user;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.github.paulushcgcj.mentorship.models.IdentifiableEntry;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceUser implements IdentifiableEntry<ServiceUser> {

  private String id;
  private Gender gender;
  private Name name;
  private AddressLocation location;
  private String email;
  private UserPrincipalInfo login;
  private ZonedDateTime dob;
  private ZonedDateTime registered;
  private String phone;
  private String cell;
  private ImagesInformation picture;
  private String nat;


  public static List<String> comparingFields() {
    return Arrays.asList(
      "name",
      "email",
      "nat",
      "gender"
    );
  }
}
