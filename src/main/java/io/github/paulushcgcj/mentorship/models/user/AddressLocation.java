package io.github.paulushcgcj.mentorship.models.user;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.github.paulushcgcj.mentorship.models.company.Position;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressLocation {
  private StreetAddress street;
  private String city;
  private String state;
  private String country;
  private String postcode;
  private Position coordinates;
  private TimeZoneInfo timezone;
}
