package org.gestion.proyecto_sanitario.auth.repository;

import org.gestion.proyecto_sanitario.auth.model.RefreshToken;
import org.gestion.proyecto_sanitario.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);
}
