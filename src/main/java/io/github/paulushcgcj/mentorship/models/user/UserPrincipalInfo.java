package io.github.paulushcgcj.mentorship.models.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "users")
public class UserPrincipalInfo implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private UUID uuid;
  private String username;
  private String password;
  private String salt;
  private boolean enabled;
  @Transient
  private String md5;
  @Transient
  private String sha1;
  @Transient
  private String sha256;

  @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
  @JoinTable(
    name = "users_roles",
    joinColumns = { @JoinColumn(name = "user_id") }, // <- meu nome na outra tabela
    inverseJoinColumns = {@JoinColumn(name = "role_id")} // <- o nome do outro na outra tabela
  )
  private Set<UserGroup> groups = new HashSet<>();

  @Override
  public List<UserGroup> getAuthorities() {
    return new ArrayList<>(groups);
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

}
