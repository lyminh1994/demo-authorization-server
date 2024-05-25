package dev.hobie.authorizationservice.repository;

import dev.hobie.authorizationservice.model.AuthorizationConsent;
import dev.hobie.authorizationservice.model.AuthorizationConsent.AuthorizationConsentId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationConsentRepository
    extends JpaRepository<AuthorizationConsent, AuthorizationConsentId> {

  Optional<AuthorizationConsent> findByRegisteredClientIdAndPrincipalName(
      String registeredClientId, String principalName);

  void deleteByRegisteredClientIdAndPrincipalName(String registeredClientId, String principalName);
}
