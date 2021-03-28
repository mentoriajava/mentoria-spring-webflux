
package io.github.paulushcgcj.mentorship.models.user;

import io.github.paulushcgcj.mentorship.models.IdentifiableEntry;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

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

}
