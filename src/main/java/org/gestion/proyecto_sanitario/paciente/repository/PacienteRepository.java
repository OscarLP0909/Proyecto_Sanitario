package org.gestion.proyecto_sanitario.paciente.repository;

import org.gestion.proyecto_sanitario.paciente.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    // Buscar paciente por nif
    Optional<Paciente> findByNif(String nif);

    // Buscar por user
    Optional<Paciente> findByUserId(Long userId);
}
