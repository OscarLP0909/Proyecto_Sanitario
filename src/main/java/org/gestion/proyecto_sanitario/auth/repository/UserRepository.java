package org.gestion.proyecto_sanitario.auth.repository;

import org.gestion.proyecto_sanitario.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
