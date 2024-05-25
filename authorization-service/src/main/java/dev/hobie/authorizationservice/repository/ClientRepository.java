package dev.hobie.authorizationservice.repository;

import dev.hobie.authorizationservice.model.Client;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

  Optional<Client> findByClientId(String clientId);
}
