package dev.hobie.authorizationservice.repository;

import dev.hobie.authorizationservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {}
