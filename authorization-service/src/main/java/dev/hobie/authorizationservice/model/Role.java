package dev.hobie.authorizationservice.model;

import dev.hobie.authorizationservice.model.Role.RoleId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "authorities")
@IdClass(RoleId.class)
public class Role {

  @Id @NotNull private String username;

  @Id
  @Size(max = 50)
  @NotNull
  @Column(name = "authority", nullable = false, length = 50)
  private String authority;

  @Getter
  @Setter
  @EqualsAndHashCode
  public static class RoleId implements Serializable {

    private String username;
    private String authority;
  }
}
