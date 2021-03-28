package io.github.paulushcgcj.mentorship.models.user;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPrincipalInfo {
  private UUID uuid;
  private String username;
  private String password;
  private String salt;
  private String md5;
  private String sha1;
  private String sha256;
}
