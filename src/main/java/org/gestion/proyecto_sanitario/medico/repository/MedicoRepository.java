package org.gestion.proyecto_sanitario.medico.repository;

import org.gestion.proyecto_sanitario.medico.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    // Buscar médico por nif
    Optional<Medico> findByNif(String nif);

    // Buscar médicos por especialidad
    List<Medico> findByEspecialidadesId(Long especialidadId);
}
