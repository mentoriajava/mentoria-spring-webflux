package io.github.paulushcgcj.mentorship.models.user;

import io.github.paulushcgcj.mentorship.models.company.Position;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;

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
