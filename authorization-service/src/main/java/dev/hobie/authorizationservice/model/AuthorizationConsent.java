package dev.hobie.authorizationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "oauth2_authorization_consent")
@IdClass(AuthorizationConsent.AuthorizationConsentId.class)
public class AuthorizationConsent {

  @Id private String registeredClientId;
  @Id private String principalName;

  @Column(length = 1000)
  private String authorities;

  @Getter
  @Setter
  @EqualsAndHashCode
  public static class AuthorizationConsentId implements Serializable {

    private String registeredClientId;
    private String principalName;
  }
}
