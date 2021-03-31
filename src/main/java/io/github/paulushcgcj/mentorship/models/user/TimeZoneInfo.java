package io.github.paulushcgcj.mentorship.models.user;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeZoneInfo {
  private String offset;
  private String description;
}
