package dev.hobie.authorizationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements Serializable {

  @Id
  @Size(max = 200)
  @Column(name = "username", nullable = false, length = 200)
  private String username;

  @NotNull
  @Size(max = 500)
  @Column(name = "password", nullable = false, length = 500)
  private String password;

  @NotNull
  @Column(name = "enabled", nullable = false)
  private boolean enabled;

  @OneToMany(mappedBy = "username")
  private Set<Role> authorities = new LinkedHashSet<>();
}
