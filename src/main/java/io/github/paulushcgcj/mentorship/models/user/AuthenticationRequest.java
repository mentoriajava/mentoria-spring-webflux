package io.github.paulushcgcj.mentorship.models.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationRequest {
  private String username;
  private String password;
}
