package io.github.paulushcgcj.mentorship.models.user;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;

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
