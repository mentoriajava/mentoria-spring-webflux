package io.github.paulushcgcj.mentorship.models.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "roles")
public class UserGroup implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;

  @Override
  public String getAuthority() {
    return name;
  }
}
