package dev.hobie.authorizationservice.repository;

import dev.hobie.authorizationservice.model.Role;
import dev.hobie.authorizationservice.model.Role.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Role, RoleId> {}
